package com.zhong;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.Serializable;
import java.util.Arrays;

/**
 * master key
 *
 * @author 张中俊
 **/
public class MasterKey implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 大素数
     */
    private long p;
    /**
     * 大素数
     */
    private long q;
    /**
     * 用来建立索引x
     */
    private byte[] Kx = null;
    /**
     * 用来建立索引y
     */
    private byte[] KI = null;
    /**
     * 用来建立索引y
     */
    private byte[] Kz = null;
    /**
     * 用来生成stag
     */
    private byte[] Ks = null;
    /**
     * 来自Zn域
     */
    private SerializableElement g1 = null;
    /**
     * 来自Zn域
     */
    private SerializableElement g2 = null;
    /**
     * 来自Zn域
     */
    private SerializableElement g3 = null;

    /**
     * 构造函数
     *
     * @param p
     *         大素数
     * @param q
     *         大素数
     * @param kx
     *         用来建立索引x
     * @param KI
     *         用来建立索引y
     * @param kz
     *         用来建立索引y
     * @param ks
     *         用来生成stag
     * @param g1
     *         来自Zn域
     * @param g2
     *         来自Zn域
     * @param g3
     *         来自Zn域
     */
    public MasterKey(long p, long q, byte[] kx, byte[] KI, byte[] kz, byte[] ks, SerializableElement g1, SerializableElement g2, SerializableElement g3) {
        this.p = p;
        this.q = q;
        Kx = kx;
        this.KI = KI;
        Kz = kz;
        Ks = ks;
        this.g1 = g1;
        this.g2 = g2;
        this.g3 = g3;
    }

    /**
     * 产生MasterKey 并且将其序列化到文件中
     */
    public static void generateMasterKey() {
        /**
         * 双线性对
         */
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
        SerializableElement g1 = new SerializableElement(pairing.getZr().newRandomElement());
        SerializableElement g2 = new SerializableElement(pairing.getZr().newRandomElement());
        SerializableElement g3 = new SerializableElement(pairing.getZr().newRandomElement());

        byte[] KI = CryptoPrimitives.randomBytes(128 / 8);
        byte[] Kz = CryptoPrimitives.randomBytes(128 / 8);
        byte[] Kx = CryptoPrimitives.randomBytes(128 / 8);
        byte[] Ks = CryptoPrimitives.randomBytes(128 / 8);

        MasterKey mk = new MasterKey(0, 0, Kx, KI, Kz, Ks, g1, g2, g3);

        String masterKeyFile = MyUtils.getFile("keys", "master.key").getAbsolutePath();
        SerializationDemonstrator.serialize(mk, masterKeyFile);
    }

    /**
     * 从文件中读取MasterKey
     *
     * @return 读取到的Master Key
     */
    public static MasterKey getMasterKey() {
        String masterKeyFile = MyUtils.getFile("keys", "master.key").getAbsolutePath();
        return SerializationDemonstrator.deserialize(masterKeyFile);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getP() {
        return p;
    }

    public long getQ() {
        return q;
    }

    public byte[] getKx() {
        return Kx;
    }

    public byte[] getKI() {
        return KI;
    }

    public byte[] getKz() {
        return Kz;
    }

    public byte[] getKs() {
        return Ks;
    }

    public SerializableElement getG1() {
        return g1;
    }

    public SerializableElement getG2() {
        return g2;
    }

    public SerializableElement getG3() {
        return g3;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===========您正在查看MasterKey的信息===========");
        sb.append(System.lineSeparator());
        sb.append("p: " + p);
        sb.append(System.lineSeparator());
        sb.append("q: " + q);
        sb.append(System.lineSeparator());
        sb.append("Kx: " + Arrays.toString(Kx));
        sb.append(System.lineSeparator());
        sb.append("KI: " + Arrays.toString(KI));
        sb.append(System.lineSeparator());
        sb.append("Kz: " + Arrays.toString(Kz));
        sb.append(System.lineSeparator());
        sb.append("Ks: " + Arrays.toString(Ks));
        sb.append(System.lineSeparator());
        sb.append("g1: " + g1.getElement().toString());
        sb.append(System.lineSeparator());
        sb.append("g2: " + g2.getElement().toString());
        sb.append(System.lineSeparator());
        sb.append("g3: " + g3.getElement().toString());
        sb.append(System.lineSeparator());
        sb.append("=================================================");
        return sb.toString();
    }
}
