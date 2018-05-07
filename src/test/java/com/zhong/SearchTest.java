package com.zhong;

import co.junwei.bswabe.BswabePrv;
import co.junwei.bswabe.BswabePub;
import co.junwei.bswabe.SerializeUtils;
import co.junwei.cpabe.Common;
import co.junwei.cpabe.Cpabe;
import com.zhong.rm.MysqlConnPool;
import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author 张中俊
 * @create 2018-04-03 11:45
 **/
public class SearchTest {
    @Test
    public void tokenGenrationTest() throws Exception {
        ArrayList<String> ws = new ArrayList<>();
        ws.add("keyword1");
        ws.add("keyword2");
        ST st = TokenGeneration.TokenGen(ws, 3);
        System.out.println(Arrays.toString(st.getStag()));
        for (int i = 0; i < st.getXtoken().length; i++) {
            for (int j = 0; j < st.getXtoken()[i].length; j++) {
                Element e = st.getXtoken()[i][j];
                System.out.println(e.toString());
            }
        }
    }

    @Test
    public void searchTest() throws Exception {
        ArrayList<String> ws = new ArrayList<>();
        ws.add("keyword1");
        ws.add("keyword2");
        ST st = TokenGeneration.TokenGen(ws, 3);

        ArrayList<ArrayList<byte[]>> res = SearchAlgorithm.search_server(st);
        StringBuilder sb = new StringBuilder();
        sb.append("搜索结果中有 " + res.size() + " 个结果");
        sb.append(System.lineSeparator());
        int i = 0;
        for (ArrayList<byte[]> r : res) {
            sb.append("第 " + i + " 个:");
            sb.append(System.lineSeparator());
            sb.append("\t" + "密文1：" + Arrays.toString(r.get(0)));
            sb.append(System.lineSeparator());
            sb.append("\t" + "密文2：" + Arrays.toString(r.get(1)));
            sb.append(System.lineSeparator());
            i++;
        }
        System.out.println(sb.toString());
    }

    @Test
    public void decTest() throws Exception {
        Cpabe cpabe = new Cpabe();
        ArrayList<String> ws = new ArrayList<>();
        //ws.add("keyword1");
        ws.add("keyword2");
        ST st = TokenGeneration.TokenGen(ws, 3);
        String pubfile = MyUtils.getFile("abe_keys", "pub_key").getAbsolutePath();
        String prvfile = MyUtils.getFile("abe_keys", "prv_key").getAbsolutePath();
        BswabePub pub = null;
        BswabePrv prv = null;
        /* get BswabePub from pubfile */
        try {
            byte[] pub_byte = Common.suckFile(pubfile);
            pub = SerializeUtils.unserializeBswabePub(pub_byte);
            byte[] prv_byte = Common.suckFile(prvfile);
            prv = SerializeUtils.unserializeBswabePrv(pub, prv_byte);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection conn = MysqlConnPool.getConnection();
        ArrayList<ArrayList<byte[]>> res = SearchAlgorithm.search_server(st);
        StringBuilder sb = new StringBuilder();
        sb.append("搜索结果中有 " + res.size() + " 个结果");
        sb.append(System.lineSeparator());
        int i = 0;

        for (ArrayList<byte[]> r : res) {
            sb.append("第 " + i + " 个:");
            sb.append(cpabe.dec(pub, prv, r));
            sb.append(System.lineSeparator());
            i++;
        }
        System.out.println(sb.toString());
    }
}
