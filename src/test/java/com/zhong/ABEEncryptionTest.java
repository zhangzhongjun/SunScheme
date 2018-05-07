package com.zhong;

import co.junwei.bswabe.BswabePub;
import co.junwei.bswabe.SerializeUtils;
import co.junwei.cpabe.Common;
import co.junwei.cpabe.Cpabe;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author 张中俊
 * @create 2018-04-16 5:15
 **/
public class ABEEncryptionTest {
    /**
     * 双线性对
     */
    final static public Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
    public static MasterKey mk = null;
    public static PublicKey pk = null;

    //公钥
    static BswabePub pub = null;
    //策略
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

    //测试abe加密的密文的大小
    @Test
    public void t1() throws Exception {
        String filename = "hello world";
        ArrayList<byte[]> e = cpabe.enc(pub, policy, filename.getBytes("utf-8"));
        int temp = 0;
        for (byte[] t : e) {
            temp = temp + t.length;
        }
        System.out.println(temp);
    }

}
