package com.zhong;

import co.junwei.bswabe.BswabePub;
import co.junwei.bswabe.SerializeUtils;
import co.junwei.cpabe.Common;
import co.junwei.cpabe.Cpabe;
import com.zhong.concurrent.Task;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * 算法1：生成索引文件
 *
 * @author 张中俊
 */
public class EDBSetup {
    /**
     * 双线性对
     */
    final static public Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
    public static MasterKey mk = null;
    public static PublicKey pk = null;

    static BswabePub pub = null;

    static String policy = "sn:student2 cn:student2 uid:student2 3of3";
    // 使用基于属性的加密
    static Cpabe cpabe = new Cpabe();

    static {
        mk = MasterKey.getMasterKey();
        pk = PublicKey.getPublicKey();
        String pubfile = MyUtils.getFile("abe_keys", "pub_key").getAbsolutePath();
        try {
            byte[] pub_byte = Common.suckFile(pubfile);
            pub = SerializeUtils.unserializeBswabePub(pub_byte);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 算法1
     *
     * @param keyword
     *         要生成索引的关键字
     * @param filenames
     *         要生成索引的文件名
     *
     * @return 生成的索引
     *
     * @throws Exception
     *         抛出的异常
     */
    public static Task EDBSetup(String keyword, Collection<String> filenames) throws Exception {
        /**
         * TSet
         */
        ArrayList<TSet> TSets = new ArrayList<>();
        /**
         * XSet
         */
        ArrayList<String> XSets = new ArrayList<String>();

        Element w = pairing.getZr().newElement();
        w.setFromBytes(keyword.getBytes("utf-8"));
        Element w_inv = w.duplicate().invert();
        byte[] stagw = MyUtils.F(mk.getKs(), mk.getG1().getElement().duplicate().powZn(w_inv));
        // 计数器 用于计数该关键词对应的文件个数
        int c = 0;
        for (String filename : filenames) {
            byte[] l = MyUtils.F(stagw, c + "");

            ArrayList<byte[]> e = cpabe.enc(pub, policy, filename.getBytes("utf-8"));
            Element xind = MyUtils.Fp(mk.getKI(), filename);
            Element z = MyUtils.Fp(mk.getKz(), mk.getG2().getElement().duplicate().powZn(w_inv.duplicate()).toString() + c);
            Element y = xind.duplicate().mul(z.duplicate().invert());
            TSet tSet = new TSet(Arrays.toString(l), e, new SerializableElement(y), keyword);
            TSets.add(tSet);
            // 计算XSet
            Element t = MyUtils.Fp(mk.getKx(), mk.getG3().getElement().duplicate().powZn(w_inv.duplicate()).toString());
            Element tt = t.duplicate().mul(xind.duplicate());
            Element xtagw = pk.getG().getElement().duplicate().powZn(tt.duplicate());
            XSets.add(xtagw.toString());
            c++;
        }
        return new Task(TSets, XSets);
    }
}
