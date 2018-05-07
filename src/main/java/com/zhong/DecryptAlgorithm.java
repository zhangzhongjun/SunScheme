package com.zhong;

import co.junwei.bswabe.BswabePrv;
import co.junwei.bswabe.BswabePub;
import co.junwei.bswabe.SerializeUtils;
import co.junwei.cpabe.Common;
import co.junwei.cpabe.Cpabe;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 解密算法
 *
 * @author 张中俊
 **/
public class DecryptAlgorithm {
    static BswabePub pub = null;
    static BswabePrv prv = null;
    static String policy = "sn:student2 cn:student2 uid:student2 3of3";

    static {
        String pubfile = MyUtils.getFile("abe_keys", "pub_key").getAbsolutePath();
        String prvfile = MyUtils.getFile("abe_keys", "prv_key").getAbsolutePath();
        /* get BswabePub from pubfile */
        try {
            byte[] pub_byte = Common.suckFile(pubfile);
            pub = SerializeUtils.unserializeBswabePub(pub_byte);
            byte[] prv_byte = Common.suckFile(prvfile);
            prv = SerializeUtils.unserializeBswabePrv(pub, prv_byte);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密
     *
     * @param es
     *         密文的数组
     *
     * @return 明文的数组
     *
     * @throws Exception
     *         异常
     */
    public static ArrayList<String> decrypt(ArrayList<ArrayList<byte[]>> es) throws Exception {
        Cpabe cpabe = new Cpabe();
        ArrayList<String> res = new ArrayList<>();
        for (ArrayList<byte[]> e : es) {
            String s = cpabe.dec(pub, prv, e);
            res.add(s);
        }
        return res;
    }
}
