package com.zhong.rm;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.net.SocketTimeoutException;
import java.util.*;

/**
 * redis包装类
 *
 * @author 张中俊
 **/
public class RedisWrapper {
    //redis服务器连接
    private static Jedis jedis;

    static {
        jedis = RedisWrapper.getJedisObject();
    }

    /**
     * 获得Jedis对象
     *
     * @return jedis连接
     */
    public static Jedis getJedisObject() {
        //连接本地的 Redis 服务
        Jedis jedis = RedisPool.getJedis();
        System.out.println("连接redis服务器成功");
        //查看服务是否运行
        if ("PONG".equals(jedis.ping())) {
            return jedis;
        } else {
            return null;
        }
    }

    /**
     * 获得Jedis对象，加入了失败重连机制
     *
     * @return jedis连接
     */
    public static Jedis getJedisObject2() {
        int timeoutCount = 0;
        while (true) // 如果是网络超时则多试几次
        {
            try {
                Jedis jedis = RedisPool.getJedis();
                return jedis;
            } catch (Exception e) {
                // 底层原因是SocketTimeoutException，不过redis已经捕捉且抛出JedisConnectionException，不继承于前者
                if (e instanceof JedisConnectionException || e instanceof SocketTimeoutException) {
                    timeoutCount++;
                    //失败次数超过10次
                    if (timeoutCount > 10) {
                        break;
                    }
                } else {
                    System.out.println("getJedis error" + e);
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 获得第begin 到 第end个关键词的记录<br>
     * 在函数的内部获得连接池的资源，使用完毕之后直接释放<br>
     *
     * @param begin
     *         起始，含，从0开始
     * @param end
     *         结束，不含，从0开始
     *
     * @return 获得begin到end的关键词-文件名的记录
     */
    public static Map<String, Collection<String>> getRecords(int begin, int end) {
        List<String> allKeys = jedis.lrange("allKeys", begin, end - 1);
        Map<String, Collection<String>> res = new HashMap<>();
        for (String key : allKeys) {
            Set<String> filenames = jedis.smembers(key);
            res.put(key, filenames);
        }
        //System.out.println("加载数据成功，已经加载 "+(end-begin)+" 个关键词的记录");
        return res;
    }


    /**
     * 获得第begin 到 第end个关键词的记录<br>
     * 在函数的内部获得连接池的资源，使用完毕之后直接释放<br>
     *
     * @param begin
     *         起始，含，从0开始
     * @param end
     *         结束，不含，从0开始
     */
    public static void getRecords2(int begin, int end) {
        System.out.println("正在访问" + begin + "-" + end);
    }


}