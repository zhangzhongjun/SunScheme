package com.zhong.rm;

/**
 * @author 张中俊
 * @create 2018-04-03 8:38
 **/

import bloomfilter.mutable.BloomFilter;
import com.zhong.MyUtils;
import com.zhong.SerializableElement;
import com.zhong.SerializationDemonstrator;
import com.zhong.TSet;
import com.zhong.concurrent.Task;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * mysql的wrapper
 *
 * @author 张中俊
 **/
public class MysqlHelper {
    private static Connection conn;                                      //连接
    private static PreparedStatement pres;                                      //PreparedStatement对象
    private static BloomFilter<byte[]> bf;

    static {
        File file = MyUtils.getFile("tsets_bloom_filter", "xset_bf_10000000.bf");
        bf = SerializationDemonstrator.deserialize(file.getAbsolutePath());

        try {
            conn = MysqlConnPool.getConnection();
            System.out.println("数据库连接成功!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行update、insert、delete等语句，返回值为受影响的行数
     *
     * @param sql
     *         sql语句
     *
     * @return 受到影响的行数
     */
    public static int executeUpdate(String sql) {
        int resCount = 0;
        if (sql == null || sql.isEmpty()) {
            System.out.println("sql语句不能为空");
            return resCount;
        }
        PreparedStatement ps = null;
        System.out.println("sql--> " + sql);
        try {
            ps = conn.prepareStatement(sql);
            resCount = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
        return resCount;
    }

    /**
     * 执行能够返回结果集的查询语句
     *
     * @param sql
     *         要执行的sql语句
     *
     * @return 查询结果集
     */
    public static ResultSet executeQuery(String sql) {
        if (sql.isEmpty()) {
            System.out.println("sql语句不为空");
            return null;
        }
        ResultSet rs = null;
        PreparedStatement ps = null;
        System.out.println("sql--> " + sql);
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 向数据库中的表TSets中插入数据
     *
     * @param task
     *         要插入的数据
     */
    public static void saveTask(Task task) {
        String sql_TSets = "insert into TSets(label,e,y,keyword) values(?,?,?,?)";
        String sql_XSets = "insert into XSets(xSet) values(?)";
        try {
            //开始保存XSet
            pres = conn.prepareStatement(sql_XSets);
            for (int i = 0; i < task.getxSets().size(); i++) {
                pres.setString(1, task.getxSets().get(i));
                pres.addBatch(); //实现批量插入
            }
            pres.executeBatch();//批量插入到数据库中

            //开始保存TSets
            pres = conn.prepareStatement(sql_TSets);
            for (int i = 0; i < task.gettSets().size(); i++) {
                pres.setString(1, task.gettSets().get(i).getL());
                pres.setBytes(2, MyUtils.msg2Byte(task.gettSets().get(i).getE()));
                pres.setBytes(3, MyUtils.msg2Byte(task.gettSets().get(i).getY()));
                pres.setString(4, task.gettSets().get(i).getKeyword());
                pres.addBatch(); //实现批量插入
            }
            pres.executeBatch();//批量插入到数据库中

            if (pres != null)
                pres.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从表TSets中获得数据
     *
     * @param label
     *         要查询的TSets的label
     *
     * @return 该label对应的TSet
     */
    public static TSet getTask(String label) {
        String sql_TSets = "select * from TSets where label=(?) limit 1";
        TSet tSet = null;
        try {
            //开始保存XSet
            pres = conn.prepareStatement(sql_TSets);
            pres.setString(1, label);
            ResultSet res = pres.executeQuery();
            while (res.next()) {
                String l = res.getString(1);
                byte[] e_bytes = res.getBytes(2);
                ArrayList<byte[]> e = (ArrayList<byte[]>) MyUtils.byte2Msg(e_bytes);
                byte[] y_bytes = res.getBytes(3);
                SerializableElement y = (SerializableElement) MyUtils.byte2Msg(y_bytes);
                String keyword = res.getString(4);
                tSet = new TSet(l, e, y, keyword);
            }
            if (pres != null)
                pres.close();
            return tSet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从指定表中获得数据
     *
     * @param label
     *         要查询的TSets的label
     * @param tableName
     *         表名
     *
     * @return
     */
    public static TSet getTask(String label, String tableName) {
        String sql_TSets = "select * from " + tableName + " where label=(?) limit 1";
        TSet tSet = null;
        try {
            //开始保存XSet
            pres = conn.prepareStatement(sql_TSets);
            pres.setString(1, label);
            ResultSet res = pres.executeQuery();
            while (res.next()) {
                String l = res.getString(1);
                byte[] e_bytes = res.getBytes(2);
                ArrayList<byte[]> e = (ArrayList<byte[]>) MyUtils.byte2Msg(e_bytes);
                byte[] y_bytes = res.getBytes(3);
                SerializableElement y = (SerializableElement) MyUtils.byte2Msg(y_bytes);
                tSet = new TSet(l, e, y, null);
            }
            if (pres != null)
                pres.close();
            return tSet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过查询数据表判断key是否在XSets中
     *
     * @param key
     *         要查询的关键词
     *
     * @return 是否在表Xsets中
     */
    public static boolean isInXSets(String key) {
        Task task = null;
        String sql_XSets = "select count(*) from XSets WHERE xSet=(?) limit 1";
        try {
            pres = conn.prepareStatement(sql_XSets);
            pres.setString(1, key);

            ResultSet res = pres.executeQuery();
            res.next();
            int count = res.getInt(1);

            if (pres != null)
                pres.close();

            if (count == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 使用bloom filter测试xset是否在表XSet中
     *
     * @param key
     *         要测试的数据库
     *
     * @return key是否包含在bloom filter中
     *
     * @throws UnsupportedEncodingException
     *         异常
     */
    public static boolean isInXSets_v2(String key) throws UnsupportedEncodingException {
        return bf.mightContain(key.getBytes("utf-8"));
    }

    /**
     * 获得表Xsets中的内容
     *
     * @param offset
     *         偏移量
     * @param row_count
     *         要获得的行数
     *
     * @return Xsets中的内容
     */
    public ArrayList<String> getXSets(int offset, int row_count) {
        ArrayList<String> xSets = new ArrayList<>();
        Task task = null;
        String sql_XSets = "select * from XSets limit " + offset + "," + row_count;
        try {
            pres = conn.prepareStatement(sql_XSets);

            ResultSet res = pres.executeQuery();
            while (res.next()) {
                String xSet = res.getString(1);
                xSets.add(xSet);
            }
            if (pres != null)
                pres.close();
            return xSets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

