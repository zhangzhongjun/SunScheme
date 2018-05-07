package com.zhong;

import com.zhong.rm.MysqlHelper;
import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 算法3：搜索的过程
 */
public class SearchAlgorithm {
    public static MasterKey mk = null;
    public static PublicKey pk = null;

    static {
        mk = MasterKey.getMasterKey();
        pk = PublicKey.getPublicKey();
    }

    /**
     * 搜索中服务器需要做的事情
     *
     * @param st
     *         客户端提交的搜索令牌
     *
     * @return 搜索的结果，是一个list，list的每一个元素是含有两个byte[]的数组
     *
     * @throws Exception
     *         异常
     */
    public static ArrayList<ArrayList<byte[]>> search_server(ST st) throws Exception {
        //搜索的结果
        ArrayList<ArrayList<byte[]>> es = new ArrayList<>();
        int c = 0;
        byte[] stag = st.getStag();
        while (true) {
            byte[] l = MyUtils.F(stag, c + "");

            String label = Arrays.toString(l);
            TSet tSet = MysqlHelper.getTask(label);
            if (tSet == null) {
                return es;
            }
            Element y = tSet.getY().getElement();
            //要求全部是真
            boolean flag2 = true;
            for (int i = 0; i < st.getXtoken()[c].length; i++) {
                Element token = st.getXtoken()[c][i].duplicate();
                Element x = token.duplicate().powZn(y.duplicate());
                flag2 = flag2 && MysqlHelper.isInXSets_v2(x.toString());
                if (!flag2) {
                    break;
                }
            }
            if (flag2) {
                es.add(tSet.getE());
            }
            c = c + 1;
        }
    }


    /**
     * 搜索中服务器需要做的事情
     *
     * @param st
     *         客户端提交的搜索令牌
     * @param tableName
     *         要搜索的表的名字
     *
     * @return 搜索的结果，是一个list，list的每一个元素是含有两个byte[]的数组
     *
     * @throws Exception
     *         异常
     */
    public static ArrayList<ArrayList<byte[]>> search_server(ST st, String tableName) throws Exception {
        //搜索的结果
        ArrayList<ArrayList<byte[]>> es = new ArrayList<>();
        int c = 0;
        byte[] stag = st.getStag();
        while (true) {
            byte[] l = MyUtils.F(stag, c + "");

            String label = Arrays.toString(l);
            TSet tSet = MysqlHelper.getTask(label, tableName);
            if (tSet == null) {
                return es;
            }
            Element y = tSet.getY().getElement();
            //要求全部是真
            boolean flag2 = true;
            for (int i = 0; i < st.getXtoken()[c].length; i++) {
                Element token = st.getXtoken()[c][i].duplicate();
                Element x = token.duplicate().powZn(y.duplicate());
                flag2 = flag2 && MysqlHelper.isInXSets_v2(x.toString());
                if (!flag2) {
                    break;
                }
            }
            if (flag2) {
                es.add(tSet.getE());
            }
            c = c + 1;
        }
    }

}