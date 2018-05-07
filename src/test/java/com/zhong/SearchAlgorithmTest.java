package com.zhong;

import com.zhong.rm.MysqlConnPool;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 张中俊
 * @create 2018-04-04 20:48
 **/
public class SearchAlgorithmTest {
    @Test
    public void search_serverTest() throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add("key1");
        ST st = TokenGeneration.TokenGen(kws, 3);
        Connection conn = MysqlConnPool.getConnection();
        ArrayList<ArrayList<byte[]>> miwens = SearchAlgorithm.search_server(st);
        ArrayList<String> mingwens = DecryptAlgorithm.decrypt(miwens);
        for (String mingwen : mingwens) {
            System.out.println(mingwen);
        }
    }

    @Test
    public void search_serverTest2() throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add("key1");
        kws.add("key2");
        ST st = TokenGeneration.TokenGen(kws, 3);
        Connection conn = MysqlConnPool.getConnection();
        ArrayList<ArrayList<byte[]>> miwens = SearchAlgorithm.search_server(st);
        ArrayList<String> mingwens = DecryptAlgorithm.decrypt(miwens);
        for (String mingwen : mingwens) {
            System.out.println(mingwen);
        }
    }


    @Test
    public void search_serverTest3() throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add("key1");
        kws.add("key2");
        kws.add("key3");
        ST st = TokenGeneration.TokenGen(kws, 3);
        Connection conn = MysqlConnPool.getConnection();
        ArrayList<ArrayList<byte[]>> miwens = SearchAlgorithm.search_server(st);
        ArrayList<String> mingwens = DecryptAlgorithm.decrypt(miwens);
        for (String mingwen : mingwens) {
            System.out.println(mingwen);
        }
    }


    @Test
    public void search_serverTest4() throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add("clubnumber10");
        kws.add("clubnumber11");
        kws.add("clubnumber13");
        ST st = TokenGeneration.TokenGen(kws, 9);
        Connection conn = MysqlConnPool.getConnection();
        ArrayList<ArrayList<byte[]>> miwens = SearchAlgorithm.search_server(st);
        ArrayList<String> mingwens = DecryptAlgorithm.decrypt(miwens);
        //[24110124, 24303115, 24329175, 24965832, 25101213, 25121290, 25345551, 25491023, 25703743]
        System.out.println(mingwens);
    }

    @Test
    public void search_serverTest5() throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add("oP");
        kws.add("nk");

        ST st = TokenGeneration.TokenGen(kws, 4);
        Connection conn = MysqlConnPool.getConnection();
        ArrayList<ArrayList<byte[]>> miwens = SearchAlgorithm.search_server(st);
        ArrayList<String> mingwens = DecryptAlgorithm.decrypt(miwens);
        //正确结果 [24171206]
        System.out.println(mingwens);
    }

    @Test
    public void search_serverTest6() throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add("Kaskade");
        ST st = TokenGeneration.TokenGen(kws, 19);
        Connection conn = MysqlConnPool.getConnection();
        ArrayList<ArrayList<byte[]>> miwens = SearchAlgorithm.search_server(st);
        ArrayList<String> mingwens = DecryptAlgorithm.decrypt(miwens);
        //[23981524, 24099109, 24178847, 24450441, 24551395, 24613797, 24729201, 24972254, 25204171, 25780360, 25788552, 25885734, 26448583, 26655375, 26657797, 26665878, 26674586, 26675246, 26817827]
        System.out.println(mingwens);
    }

    @Test
    public void search_serverTest7() throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add("Condensation");
        ST st = TokenGeneration.TokenGen(kws, 13);
        Connection conn = MysqlConnPool.getConnection();
        ArrayList<ArrayList<byte[]>> miwens = SearchAlgorithm.search_server(st);
        ArrayList<String> mingwens = DecryptAlgorithm.decrypt(miwens);
        //[24007567, 24357080, 24384816, 24622594, 24710453, 25143319, 25323173, 25570544, 25767278, 25854612, 26306290, 26536063, 26601828]
        System.out.println(mingwens);
        System.out.println("一共 " + mingwens.size() + " 结果");
    }

    @Test
    public void searchProtocolTest8() throws Exception {
        //读取测试集
        File testSetFile = MyUtils.getFile("testset", "testSet.txt");
        List<String> lines = IOUtils.readLines(new FileInputStream(testSetFile));
        ArrayList<String> table_names = new ArrayList<>();
        //table_names.add("TSets_32768");
        //table_names.add("TSets_65536");
        //table_names.add("TSets_131072");
        // table_names.add("TSets_262144");
        table_names.add("TSets_524288");
        table_names.add("TSets_1048576");
        for (String table_name : table_names) {
            long sum_t = 0;
            //进行100次查询
            for (int i = 0; i < 1; i++) {
                //要查询的关键字
                List<String> kws = new ArrayList<>();
                int numLeastKeyword = Integer.MAX_VALUE;
                // 3个关键词做 并 查询
                for (int j = 0; j < 3; j++) {
                    Random random = new Random();
                    int index = random.nextInt(lines.size());
                    String t = lines.get(index);
                    String keyword = t.split(" ")[0];
                    int num = Integer.parseInt(t.split(" ")[1]);
                    if (num < numLeastKeyword) {
                        kws.add(0, keyword);
                        numLeastKeyword = num;
                    } else {
                        kws.add(keyword);
                    }
                }

                ST st = TokenGeneration.TokenGen(kws, numLeastKeyword);
                long t1 = System.currentTimeMillis();
                ArrayList<ArrayList<byte[]>> ws = SearchAlgorithm.search_server(st, table_name);
                long t2 = System.currentTimeMillis();
                sum_t = sum_t + (t2 - t1);

                //ArrayList<String> res = DecryptAlgorithm.decrypt(kws.get(0), ws);
                //System.out.println("搜索的关键词："+kws.toString());
                //System.out.println("搜索结果："+res.toString());
            }
            System.out.println("表 " + table_name + " 花费时间：" + (sum_t / 100) + " ms");
        }
    }

    @Test
    public void search_serverTest9() throws Exception {
        ArrayList<String> table_names = new ArrayList<>();
        table_names.add("TSets_32768_rand");
        table_names.add("TSets_65536_rand");
        table_names.add("TSets_131072_rand");
        table_names.add("TSets_262144_rand");
        table_names.add("TSets_524288_rand");
        table_names.add("TSets_1048576_rand");
        for (String tableName : table_names) {
            System.out.println("====正在搜索：" + tableName + "======");
            long sum_t = 0;

            //要查询的关键字
            List<String> kws = new ArrayList<>();
            kws.add("clubnumber10");
            kws.add("clubnumber11");
            kws.add("clubnumber12");

            int numLeastKeyword = 9;

            ST st = TokenGeneration.TokenGen(kws, numLeastKeyword);
            ArrayList<ArrayList<byte[]>> ws = SearchAlgorithm.search_server(st, tableName);

            //下面是解密的部分
            if (ws == null) {
                continue;
            } else {
                List<String> inds = DecryptAlgorithm.decrypt(ws);
                System.out.println("搜索结果" + ws.size());
                System.out.println("搜索的关键词：" + kws.toString());
                System.out.println("搜索结果：" + inds.toString());
            }
            System.out.println("===============");
        }
    }


    @Test
    public void search_serverTest10() throws Exception {

        ArrayList<String> table_names = new ArrayList<>();
        table_names.add("TSets_32768_rand");
        table_names.add("TSets_65536_rand");
        table_names.add("TSets_131072_rand");
        table_names.add("TSets_262144_rand");
        table_names.add("TSets_524288_rand");
        //table_names.add("TSets_1048576_rand");
        for (String tableName : table_names) {
            long sum_t = 0;
            //进行100次查询
            for (int i = 0; i < 100; i++) {
                //要查询的关键字
                List<String> kws = new ArrayList<>();
                kws.add("clubnumber10");
                kws.add("clubnumber11");
                kws.add("clubnumber12");

                int numLeastKeyword = 9;

                ST st = TokenGeneration.TokenGen(kws, numLeastKeyword);
                long t1 = System.currentTimeMillis();
                ArrayList<ArrayList<byte[]>> ws = SearchAlgorithm.search_server(st, tableName);
                long t2 = System.currentTimeMillis();
                sum_t = sum_t + (t2 - t1);

                //下面是解密的部分
                if (ws == null) {
                    continue;
                } else {
                    //List<String> inds = SearchProtocol_Split_memory.search_client_2(kws.get(0),res);
                    //System.out.println("搜索结果"+res.size());
                    //System.out.println("搜索的关键词："+kws.toString());
                    //System.out.println("搜索结果："+inds.toString());
                }
            }
            System.out.println("表 " + tableName + " 花费时间：" + (sum_t / 100) + " ms");
        }
    }

}
