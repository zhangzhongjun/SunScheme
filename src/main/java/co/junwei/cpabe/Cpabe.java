package co.junwei.cpabe;

import co.junwei.bswabe.*;
import co.junwei.cpabe.policy.LangPolicy;
import it.unisa.dia.gas.jpbc.Element;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * 未知类
 *
 * @author 张中俊
 */
public class Cpabe {

    /**
     * 生成public key和master key，并分别写入到不同的文件中
     *
     * @param pubfile
     *         装载public key的文件
     * @param mskfile
     *         装载master key的文件
     *
     * @throws IOException
     *         异常
     * @throws ClassNotFoundException
     *         异常
     */
    public void setup(String pubfile, String mskfile) throws IOException,
            ClassNotFoundException {
        byte[] pub_byte, msk_byte;
        BswabePub pub = new BswabePub();
        BswabeMsk msk = new BswabeMsk();
        Bswabe.setup(pub, msk);

        /* store BswabePub into mskfile */
        pub_byte = SerializeUtils.serializeBswabePub(pub);
        Common.spitFile(pubfile, pub_byte);

        /* store BswabeMsk into mskfile */
        msk_byte = SerializeUtils.serializeBswabeMsk(msk);
        Common.spitFile(mskfile, msk_byte);
    }

    /**
     * 根据public key , master key 以及属性 生成private key
     *
     * @param pubfile
     *         装有公钥的文件
     * @param prvfile
     *         装有私钥的文件
     * @param mskfile
     *         装有mater key的文件
     * @param attr_str
     *         属性
     *
     * @throws NoSuchAlgorithmException
     *         异常
     * @throws IOException
     *         异常
     */
    public void keygen(String pubfile, String prvfile, String mskfile,
                       String attr_str) throws NoSuchAlgorithmException, IOException {
        BswabePub pub;
        BswabeMsk msk;
        byte[] pub_byte, msk_byte, prv_byte;

        /* get BswabePub from pubfile */
        pub_byte = Common.suckFile(pubfile);
        pub = SerializeUtils.unserializeBswabePub(pub_byte);

        /* get BswabeMsk from mskfile */
        msk_byte = Common.suckFile(mskfile);
        msk = SerializeUtils.unserializeBswabeMsk(pub, msk_byte);

        String[] attr_arr = LangPolicy.parseAttribute(attr_str);
        BswabePrv prv = Bswabe.keygen(pub, msk, attr_arr);

        /* store BswabePrv into prvfile */
        prv_byte = SerializeUtils.serializeBswabePrv(prv);
        Common.spitFile(prvfile, prv_byte);
    }

    /**
     * 加密
     *
     * @param pubfile
     *         装有公钥的文件
     * @param policy
     *         策略
     * @param inputfile
     *         要加密的文明
     * @param encfile
     *         加密之后的密文
     *
     * @throws Exception
     *         异常
     */
    public void enc(String pubfile, String policy, String inputfile,
                    String encfile) throws Exception {
        BswabePub pub;
        BswabeCph cph;
        BswabeCphKey keyCph;
        byte[] plt;
        byte[] cphBuf;
        byte[] aesBuf;
        byte[] pub_byte;
        Element m;

        /* get BswabePub from pubfile */
        pub_byte = Common.suckFile(pubfile);
        pub = SerializeUtils.unserializeBswabePub(pub_byte);

        keyCph = Bswabe.enc(pub, policy);
        cph = keyCph.cph;
        m = keyCph.key;
        //System.err.println("m = " + m.toString());

        if (cph == null) {
            System.out.println("Error happed in enc");
            System.exit(0);
        }

        cphBuf = SerializeUtils.bswabeCphSerialize(cph);

        /* read file to encrypted */
        plt = Common.suckFile(inputfile);
        aesBuf = AESCoder.encrypt(m.toBytes(), plt);
        // PrintArr("element: ", m.toBytes());
        Common.writeCpabeFile(encfile, cphBuf, aesBuf);
    }

    /**
     * 加密
     *
     * @param pubfile
     *         公钥文件
     * @param policy
     *         策略
     * @param plt
     *         私钥文件
     *
     * @return 加密的结果
     *
     * @throws Exception
     *         异常
     */
    public ArrayList<byte[]> enc(String pubfile, String policy, byte[] plt) throws Exception {
        BswabePub pub;
        BswabeCph cph;
        BswabeCphKey keyCph;
        byte[] cphBuf;
        byte[] aesBuf;
        byte[] pub_byte;
        Element m;

        /* get BswabePub from pubfile */
        pub_byte = Common.suckFile(pubfile);
        pub = SerializeUtils.unserializeBswabePub(pub_byte);

        keyCph = Bswabe.enc(pub, policy);
        cph = keyCph.cph;
        m = keyCph.key;
        //System.err.println("m = " + m.toString());

        if (cph == null) {
            System.out.println("Error happed in enc");
            System.exit(0);
        }

        ArrayList<byte[]> res = new ArrayList();
        cphBuf = SerializeUtils.bswabeCphSerialize(cph);
        res.add(cphBuf);
        aesBuf = AESCoder.encrypt(m.toBytes(), plt);
        res.add(aesBuf);
        return res;
    }

    /**
     * 加密
     *
     * @param pub
     *         公钥
     * @param policy
     *         策略
     * @param plt
     *         私钥
     *
     * @return 加密结果
     *
     * @throws Exception
     *         异常
     */
    public ArrayList<byte[]> enc(BswabePub pub, String policy, byte[] plt) throws Exception {
        BswabeCph cph;
        BswabeCphKey keyCph;
        byte[] cphBuf;
        byte[] aesBuf;
        byte[] pub_byte;
        Element m;

        keyCph = Bswabe.enc(pub, policy);
        cph = keyCph.cph;
        m = keyCph.key;
        //System.err.println("m = " + m.toString());

        if (cph == null) {
            System.out.println("Error happed in enc");
            System.exit(0);
        }

        ArrayList<byte[]> res = new ArrayList();
        cphBuf = SerializeUtils.bswabeCphSerialize(cph);
        res.add(cphBuf);
        aesBuf = AESCoder.encrypt(m.toBytes(), plt);
        res.add(aesBuf);
        return res;
    }

    /**
     * 解密
     *
     * @param pubfile
     *         公钥
     * @param prvfile
     *         私钥
     * @param encfile
     *         要加密的文件
     * @param decfile
     *         要解密的文件
     *
     * @throws Exception
     *         异常
     */
    public void dec(String pubfile, String prvfile, String encfile,
                    String decfile) throws Exception {
        byte[] aesBuf, cphBuf;
        byte[] plt;
        byte[] prv_byte;
        byte[] pub_byte;
        byte[][] tmp;
        BswabeCph cph;
        BswabePrv prv;
        BswabePub pub;

        /* get BswabePub from pubfile */
        pub_byte = Common.suckFile(pubfile);
        pub = SerializeUtils.unserializeBswabePub(pub_byte);

        /* read ciphertext */
        tmp = Common.readCpabeFile(encfile);
        aesBuf = tmp[0];
        cphBuf = tmp[1];
        cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

        /* get BswabePrv form prvfile */
        prv_byte = Common.suckFile(prvfile);
        prv = SerializeUtils.unserializeBswabePrv(pub, prv_byte);

        BswabeElementBoolean beb = Bswabe.dec(pub, prv, cph);
        //System.err.println("e = " + beb.e.toString());
        if (beb.b) {
            plt = AESCoder.decrypt(beb.e.toBytes(), aesBuf);
            Common.spitFile(decfile, plt);
        } else {
            System.exit(0);
        }
    }

    /**
     * 解密
     *
     * @param pubfile
     *         公钥
     * @param prvfile
     *         私钥
     * @param tmp
     *         byte数组
     *
     * @return 返回值
     *
     * @throws Exception
     *         异常
     */
    public String dec(String pubfile, String prvfile, ArrayList<byte[]> tmp) throws Exception {
        byte[] aesBuf, cphBuf;
        byte[] plt;
        byte[] prv_byte;
        byte[] pub_byte;

        BswabeCph cph;
        BswabePrv prv;
        BswabePub pub;

        /* get BswabePub from pubfile */
        pub_byte = Common.suckFile(pubfile);
        pub = SerializeUtils.unserializeBswabePub(pub_byte);

        aesBuf = tmp.get(1);
        cphBuf = tmp.get(0);
        cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

        /* get BswabePrv form prvfile */
        prv_byte = Common.suckFile(prvfile);
        prv = SerializeUtils.unserializeBswabePrv(pub, prv_byte);

        BswabeElementBoolean beb = Bswabe.dec(pub, prv, cph);
        //System.err.println("e = " + beb.e.toString());
        if (beb.b) {
            plt = AESCoder.decrypt(beb.e.toBytes(), aesBuf);
            return new String(plt, "utf-8");
        } else {
            System.exit(0);
            return null;
        }
    }

    /**
     * 解密
     *
     * @param pub
     *         公钥
     * @param prv
     *         私钥
     * @param tmp
     *         密文+aes
     *
     * @return 解密的结果
     *
     * @throws Exception
     *         异常
     */
    public String dec(BswabePub pub, BswabePrv prv, ArrayList<byte[]> tmp) throws Exception {
        byte[] aesBuf, cphBuf;
        byte[] plt;
        byte[] prv_byte;
        byte[] pub_byte;

        BswabeCph cph;

        aesBuf = tmp.get(1);
        cphBuf = tmp.get(0);
        cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

        BswabeElementBoolean beb = Bswabe.dec(pub, prv, cph);
        //System.err.println("e = " + beb.e.toString());
        if (beb.b) {
            plt = AESCoder.decrypt(beb.e.toBytes(), aesBuf);
            return new String(plt, "utf-8");
        } else {
            System.exit(0);
            return null;
        }
    }

}
