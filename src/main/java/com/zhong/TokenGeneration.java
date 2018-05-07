package com.zhong;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 算法2：产生查询所需的token
 */
public class TokenGeneration {
    /**
     * 双线性对
     */
    final static private Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
    /**
     * master key
     */
    public static MasterKey mk = null;
    /**
     * public key
     */
    public static PublicKey pk = null;

    static {
        mk = MasterKey.getMasterKey();
        pk = PublicKey.getPublicKey();
    }

    /**
     * 生成查询所需的Token
     *
     * @param kws
     *         要搜索的关键词
     * @param count
     *         w1的文件个数
     *
     * @return 查询所需的令牌 w1的stag
     *
     * @throws Exception
     *         异常
     */
    public static ST TokenGen(List<String> kws, int count) throws Exception {
        // 计算stag
        Element w1 = pairing.getZr().newElement();
        w1.setFromBytes(kws.get(0).getBytes("utf-8"));
        Element w1_inv = w1.duplicate().invert().duplicate();
        byte[] stag = MyUtils.F(mk.getKs(), mk.getG1().getElement().duplicate().powZn(w1_inv.duplicate()));

        ArrayList<Element> w_inv_s = new ArrayList<>();
        for (int i = 0; i < kws.size(); i++) {
            Element w = pairing.getZr().newElement();
            w.setFromBytes(kws.get(i).getBytes("utf-8"));
            Element w_inv = w.duplicate().invert();
            w_inv_s.add(w_inv);
        }
        // 计算token
        Element xtoken[][] = new Element[count][kws.size()];
        // until the server stops
        for (int c = 0; c < count; c++) {
            for (int i = 0; i < kws.size(); i++) {
                Element t1 = mk.getG2().getElement().duplicate().powZn(w1_inv).duplicate();
                String t2 = t1.toString() + c;
                Element left = MyUtils.Fp(mk.getKz(), t2);

                Element w_inv = w_inv_s.get(i);
                t1 = mk.getG3().getElement().duplicate().powZn(w_inv.duplicate());
                t2 = t1.toString();
                Element right = MyUtils.Fp(mk.getKx(), t2);

                Element res = pk.getG().getElement().duplicate().powZn(left.duplicate().mul(right.duplicate()));
                xtoken[c][i] = res;
            }
        }
        return new ST(stag, xtoken);
    }
}

/**
 * SearchToken的简称，一次搜索的token
 *
 * @author 张中俊
 */
class ST {
    /**
     * 第一个关键词的stag
     */
    byte[] stag = null;
    /**
     * 搜索的令牌
     */
    Element[][] xtoken = null;

    public ST(byte[] stag, Element[][] xtoken) {
        super();
        this.stag = stag;
        this.xtoken = xtoken;
    }

    public byte[] getStag() {
        return stag;
    }

    public Element[][] getXtoken() {
        return xtoken;
    }
}