package com.zhong.rm;

import bloomfilter.CanGenerateHashFrom;
import bloomfilter.mutable.BloomFilter;
import com.zhong.EDBSetup;
import com.zhong.MyUtils;
import com.zhong.SerializationDemonstrator;
import com.zhong.concurrent.Task;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * @author 张中俊
 * @create 2018-04-10 17:40
 **/
public class MysqlHelperTest {

    @Test
    public void getXSetsTest() {
        MysqlHelper mu = new MysqlHelper();
        ArrayList<String> xSets = mu.getXSets(10, 10);
        for (String xSet : xSets) {
            System.out.println(xSet);
        }
    }

    @Test
    public void getXSetsTest2() throws Exception {
        MysqlHelper mu = new MysqlHelper();
        int offset = 0;
        long expectedElements = 10000000;
        double falsePositiveRate = 0.01;
        BloomFilter<byte[]> bf = BloomFilter.apply(
                expectedElements,
                falsePositiveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$);

        File file = MyUtils.getFile("tsets_bloom_filter", "xset_bf_10000000.bf");
        while (offset < 3000) {
            System.out.println(offset + "~" + (offset + 1000));
            ArrayList<String> xSets = mu.getXSets(offset, 1000);
            for (String xSet : xSets) {
                bf.add(xSet.getBytes("utf-8"));
            }
            offset += 1000;
        }
        SerializationDemonstrator.serialize(bf, file.getAbsolutePath());
        bf.dispose();
    }

    @Test
    public void saveTaskTest() throws Exception {
        String keyword = "key1";
        ArrayList<String> filenames = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            filenames.add("ind" + i);
        }
        Task task = EDBSetup.EDBSetup(keyword, filenames);
        Connection conn = MysqlConnPool.getConnection();
        MysqlHelper.saveTask(task);
    }

    @Test
    public void saveTaskTest2() throws Exception {
        String keyword = "key2";
        ArrayList<String> filenames = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            filenames.add("ind" + i);
        }
        Task task = EDBSetup.EDBSetup(keyword, filenames);
        Connection conn = MysqlConnPool.getConnection();
        MysqlHelper.saveTask(task);
    }

    @Test
    public void saveTaskTest3() throws Exception {
        String keyword = "key3";
        ArrayList<String> filenames = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            filenames.add("ind" + i);
        }
        Task task = EDBSetup.EDBSetup(keyword, filenames);
        Connection conn = MysqlConnPool.getConnection();
        MysqlHelper.saveTask(task);
    }
}
