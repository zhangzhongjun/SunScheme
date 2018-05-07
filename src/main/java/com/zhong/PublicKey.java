package com.zhong;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.Serializable;

/**
 * 公钥
 *
 * @author 张中俊
 **/
public class PublicKey implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 大素数
     */
    private long n;
    /**
     * 来自G
     */
    private SerializableElement g = null;

    public PublicKey(long n, SerializableElement g) {
        this.n = n;
        this.g = g;
    }

    /**
     * 产生密钥文件并写入到文件中
     */
    public static void generatePublicKey() {
        /**
         * 双线性对
         */
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
        /**
         * 群的生成元
         */
        SerializableElement g = new SerializableElement(pairing.getG1().newRandomElement());

        PublicKey pk = new PublicKey(0, g);

        String publicKeyFile = MyUtils.getFile("keys", "public.key").getAbsolutePath();
        SerializationDemonstrator.serialize(pk, publicKeyFile);
    }

    /**
     * 从文件中获取公钥
     *
     * @return 公钥
     */
    public static PublicKey getPublicKey() {
        String publicKeyFile = MyUtils.getFile("keys", "public.key").getAbsolutePath();
        return SerializationDemonstrator.deserialize(publicKeyFile);
    }

    public SerializableElement getG() {
        return g;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===============您正在查看Public Key的信息====================");
        sb.append(System.lineSeparator());
        sb.append("n: " + n);
        sb.append(System.lineSeparator());
        sb.append("g: " + g.getElement().toString());
        sb.append(System.lineSeparator());
        sb.append("==============================================");
        return sb.toString();
    }
}
