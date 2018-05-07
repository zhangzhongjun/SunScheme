package com.zhong.concurrent;

import com.zhong.EDBSetup;
import com.zhong.rm.MysqlConnPool;
import com.zhong.rm.MysqlHelper;
import com.zhong.rm.RedisWrapper;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 基于BlockingQueue实现的生产者-消费者模型
 *
 * @author 张中俊
 **/
public class BlockingQueueModel implements Model {
    //数据库连接
    private static Connection conn;
    private static Jedis jedis;
    // 访问的起点
    private static int material_begin = 75900;

    static {
        conn = MysqlConnPool.getConnection();
        jedis = RedisWrapper.getJedisObject();
    }

    private BlockingQueue<Task_material> queue_material = null;
    private BlockingQueue<Task> queue = null;

    public BlockingQueueModel(int cap) {
        // LinkedBlockingQueue 的队列是 lazy-init 的，但 ArrayBlockingQueue 在创建时就已经 init
        this.queue = new LinkedBlockingQueue<>(cap);
        this.queue_material = new LinkedBlockingQueue<>(100);
    }

    public static void main(String[] args) {
        Model model = new BlockingQueueModel(50);
        //1个原料 这里必需使用同一个对象，synchronized关键词才会生效
        Runnable runnable = model.newRunnableMaterial();
        new Thread(runnable, "原料商" + 1).start();
        //new Thread(runnable,"原料商"+2).start();

        //1个消费者
        for (int i = 0; i < 1; i++) {
            new Thread(model.newRunnableConsumer(), "消费者" + i).start();
        }
        //5个生产者
        for (int i = 0; i < 5; i++) {
            new Thread(model.newRunnableProducer(), "生产者" + i).start();
        }
    }

    @Override
    public Runnable newRunnableConsumer() {
        return new ConsumerImpl();
    }

    @Override
    public Runnable newRunnableProducer() {
        return new ProducerImpl();
    }

    @Override
    public Runnable newRunnableMaterial() {
        return new MaterialImpl();
    }

    private class MaterialImpl extends AbstractMaterial implements Material, Runnable {
        @Override
        public void material() throws InterruptedException {
            //原料的逻辑
            Map<String, Collection<String>> map = new HashMap<>();
            synchronized (this) {
                //一次读取50个文件
                System.out.println(Thread.currentThread().getName() + " 正在访问 " + material_begin + " - " + (material_begin + 50));
                map = RedisWrapper.getRecords(jedis, material_begin, material_begin + 50);
                material_begin += 50;
                for (String keyword : map.keySet()) {
                    if (map.get(keyword).size() > 200) {
                        System.out.println("包含 " + keyword + " 的文件名有 " + map.get(keyword).size() + " 个，直接舍去");
                        continue;
                    }
                    Task_material tm = new Task_material(keyword, map.get(keyword));
                    queue_material.put(tm);
                }
            }
            if (material_begin % 500 == 0) {
                System.out.println(Thread.currentThread().getName() + "已经访问 " + material_begin);
            }
        }
    }

    private class ConsumerImpl extends AbstractConsumer implements Consumer, Runnable {
        @Override
        public void consume() throws InterruptedException {
            //消费者的逻辑
            Task task = queue.take();
            MysqlHelper.saveTask(task);
            //System.out.println(Thread.currentThread().getName() + " consume: " + task+"现在 "+queue.size());
        }
    }

    private class ProducerImpl extends AbstractProducer implements Producer, Runnable {
        @Override
        public void produce() throws InterruptedException {
            //生产者的逻辑
            Task_material tm = queue_material.take();
            try {
                Task task = EDBSetup.EDBSetup(tm.getKeyword(), tm.getFilenames());
                queue.put(task);
                //System.out.println(Thread.currentThread().getName() + " produce: " + task+"现在 "+queue.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}