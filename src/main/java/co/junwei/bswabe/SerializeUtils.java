package co.junwei.bswabe;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * 序列化相关类
 *
 * @author 张中俊
 */
public class SerializeUtils {

    /**
     * 将元素序列化到byte数组中
     *
     * @param arrlist
     *         要序列化到哪个byte数组
     * @param e
     *         要序列化的元素
     */
    public static void serializeElement(ArrayList<Byte> arrlist, Element e) {
        byte[] arr_e = e.toBytes();
        serializeUint32(arrlist, arr_e.length);
        byteArrListAppend(arrlist, arr_e);
    }

    /**
     * 从byte数组中反序列化出Element
     *
     * @param arr
     *         byte数组
     * @param offset
     *         偏移量
     * @param e
     *         要反序列化到哪个元素
     *
     * @return 新的偏移量
     */
    public static int unserializeElement(byte[] arr, int offset, Element e) {
        int len;
        int i;
        byte[] e_byte;

        len = unserializeUint32(arr, offset);
        e_byte = new byte[(int) len];
        offset += 4;
        for (i = 0; i < len; i++)
            e_byte[i] = arr[offset + i];
        e.setFromBytes(e_byte);

        return (int) (offset + len);
    }

    /**
     * 将字符串序列化到byte数组
     *
     * @param arrlist
     *         要反序列化到哪个byte数组
     * @param s
     *         字符串
     */
    public static void serializeString(ArrayList<Byte> arrlist, String s) {
        byte[] b = s.getBytes();
        serializeUint32(arrlist, b.length);
        byteArrListAppend(arrlist, b);
    }

    /*
     * Usage:
     *
     * StringBuffer sb = new StringBuffer("");
     *
     * offset = unserializeString(arr, offset, sb);
     *
     * String str = sb.substring(0);
     *
     * 反序列化字符串
     *
     * @param arr byte数组
     * @param offset 偏移量
     * @param sb StringBuffer对象
     * @return 偏移量
     */
    public static int unserializeString(byte[] arr, int offset, StringBuffer sb) {
        int i;
        int len;
        byte[] str_byte;

        len = unserializeUint32(arr, offset);
        offset += 4;
        str_byte = new byte[len];
        for (i = 0; i < len; i++)
            str_byte[i] = arr[offset + i];

        sb.append(new String(str_byte));
        return offset + len;
    }

    /**
     * 将公钥序列化
     *
     * @param pub
     *         要序列化的公钥
     *
     * @return 系列化得到的byte数组
     */
    public static byte[] serializeBswabePub(BswabePub pub) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

        serializeString(arrlist, pub.pairingDesc);
        serializeElement(arrlist, pub.g);
        serializeElement(arrlist, pub.h);
        serializeElement(arrlist, pub.gp);
        serializeElement(arrlist, pub.g_hat_alpha);

        return Byte_arr2byte_arr(arrlist);
    }

    /**
     * 对公钥进行反序列化
     *
     * @param b
     *         byte数组
     *
     * @return 反序列化得到的公钥
     */
    public static BswabePub unserializeBswabePub(byte[] b) {
        BswabePub pub;
        int offset;

        pub = new BswabePub();
        offset = 0;

        StringBuffer sb = new StringBuffer("");
        offset = unserializeString(b, offset, sb);
        pub.pairingDesc = sb.substring(0);

        CurveParameters params = new DefaultCurveParameters()
                .load(new ByteArrayInputStream(pub.pairingDesc.getBytes()));
        pub.p = PairingFactory.getPairing(params);
        Pairing pairing = pub.p;

        pub.g = pairing.getG1().newElement();
        pub.h = pairing.getG1().newElement();
        pub.gp = pairing.getG2().newElement();
        pub.g_hat_alpha = pairing.getGT().newElement();

        offset = unserializeElement(b, offset, pub.g);
        offset = unserializeElement(b, offset, pub.h);
        offset = unserializeElement(b, offset, pub.gp);
        offset = unserializeElement(b, offset, pub.g_hat_alpha);

        return pub;
    }

    /**
     * 对master私钥序列化
     *
     * @param msk
     *         master私钥
     *
     * @return 序列化得到的byte数组
     */
    public static byte[] serializeBswabeMsk(BswabeMsk msk) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

        serializeElement(arrlist, msk.beta);
        serializeElement(arrlist, msk.g_alpha);

        return Byte_arr2byte_arr(arrlist);
    }

    /**
     * 对master私钥反序列化
     *
     * @param pub
     *         公钥
     * @param b
     *         byte数组
     *
     * @return 反序列化得到的master私钥
     */
    public static BswabeMsk unserializeBswabeMsk(BswabePub pub, byte[] b) {
        int offset = 0;
        BswabeMsk msk = new BswabeMsk();

        msk.beta = pub.p.getZr().newElement();
        msk.g_alpha = pub.p.getG2().newElement();

        offset = unserializeElement(b, offset, msk.beta);
        offset = unserializeElement(b, offset, msk.g_alpha);

        return msk;
    }

    /**
     * 对私钥进行序列化
     *
     * @param prv
     *         要序列化的私钥
     *
     * @return 序列化得到的byte数组
     */
    public static byte[] serializeBswabePrv(BswabePrv prv) {
        ArrayList<Byte> arrlist;
        int prvCompsLen, i;

        arrlist = new ArrayList<Byte>();
        prvCompsLen = prv.comps.size();
        serializeElement(arrlist, prv.d);
        serializeUint32(arrlist, prvCompsLen);

        for (i = 0; i < prvCompsLen; i++) {
            serializeString(arrlist, prv.comps.get(i).attr);
            serializeElement(arrlist, prv.comps.get(i).d);
            serializeElement(arrlist, prv.comps.get(i).dp);
        }

        return Byte_arr2byte_arr(arrlist);
    }

    /**
     * 反序列化私钥
     *
     * @param pub
     *         公钥
     * @param b
     *         byte数组
     *
     * @return 私钥
     */
    public static BswabePrv unserializeBswabePrv(BswabePub pub, byte[] b) {
        BswabePrv prv;
        int i, offset, len;

        prv = new BswabePrv();
        offset = 0;

        prv.d = pub.p.getG2().newElement();
        offset = unserializeElement(b, offset, prv.d);

        prv.comps = new ArrayList<BswabePrvComp>();
        len = unserializeUint32(b, offset);
        offset += 4;

        for (i = 0; i < len; i++) {
            BswabePrvComp c = new BswabePrvComp();

            StringBuffer sb = new StringBuffer("");
            offset = unserializeString(b, offset, sb);
            c.attr = sb.substring(0);

            c.d = pub.p.getG2().newElement();
            c.dp = pub.p.getG2().newElement();

            offset = unserializeElement(b, offset, c.d);
            offset = unserializeElement(b, offset, c.dp);

            prv.comps.add(c);
        }

        return prv;
    }

    /**
     * 密文序列化
     *
     * @param cph
     *         要序列化的密文
     *
     * @return 序列化得到的byte数组
     */
    public static byte[] bswabeCphSerialize(BswabeCph cph) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();
        SerializeUtils.serializeElement(arrlist, cph.cs);
        SerializeUtils.serializeElement(arrlist, cph.c);
        SerializeUtils.serializePolicy(arrlist, cph.p);

        return Byte_arr2byte_arr(arrlist);
    }

    /**
     * 密文反序列化
     *
     * @param pub
     *         公钥
     * @param cphBuf
     *         要反序列化的byte数组
     *
     * @return 密文对象
     */
    public static BswabeCph bswabeCphUnserialize(BswabePub pub, byte[] cphBuf) {
        BswabeCph cph = new BswabeCph();
        int offset = 0;
        int[] offset_arr = new int[1];

        cph.cs = pub.p.getGT().newElement();
        cph.c = pub.p.getG1().newElement();

        offset = SerializeUtils.unserializeElement(cphBuf, offset, cph.cs);
        offset = SerializeUtils.unserializeElement(cphBuf, offset, cph.c);

        offset_arr[0] = offset;
        cph.p = SerializeUtils.unserializePolicy(pub, cphBuf, offset_arr);
        offset = offset_arr[0];

        return cph;
    }

    /* Method has been test okay */
    /* potential problem: the number to be serialize is less than 2^31 */

    /**
     * 无符号的int
     *
     * @param arrlist
     *         byte数组
     * @param k
     *         未知
     */
    private static void serializeUint32(ArrayList<Byte> arrlist, int k) {
        int i;
        byte b;

        for (i = 3; i >= 0; i--) {
            b = (byte) ((k & (0x000000ff << (i * 8))) >> (i * 8));
            arrlist.add(Byte.valueOf(b));
        }
    }

    /*
     * Usage:
     *
     * You have to do offset+=4 after call this method
     */
    /* Method has been test okay */

    /**
     * 反序列化 无符号的int
     *
     * @param arr
     *         byte数组
     * @param offset
     *         偏移
     *
     * @return 整数值
     */
    private static int unserializeUint32(byte[] arr, int offset) {
        int i;
        int r = 0;

        for (i = 3; i >= 0; i--)
            r |= (byte2int(arr[offset++])) << (i * 8);
        return r;
    }

    /**
     * 序列化 策略
     *
     * @param arrlist
     *         byte数组
     * @param p
     *         要序列化的 策略
     */
    private static void serializePolicy(ArrayList<Byte> arrlist, BswabePolicy p) {
        serializeUint32(arrlist, p.k);

        if (p.children == null || p.children.length == 0) {
            serializeUint32(arrlist, 0);
            serializeString(arrlist, p.attr);
            serializeElement(arrlist, p.c);
            serializeElement(arrlist, p.cp);
        } else {
            serializeUint32(arrlist, p.children.length);
            for (int i = 0; i < p.children.length; i++)
                serializePolicy(arrlist, p.children[i]);
        }
    }

    /**
     * 反序列化策略
     *
     * @param pub
     *         公钥
     * @param arr
     *         数组
     * @param offset
     *         偏移
     *
     * @return 策略对象
     */
    private static BswabePolicy unserializePolicy(BswabePub pub, byte[] arr,
                                                  int[] offset) {
        int i;
        int n;
        BswabePolicy p = new BswabePolicy();
        p.k = unserializeUint32(arr, offset[0]);
        offset[0] += 4;
        p.attr = null;

        /* children */
        n = unserializeUint32(arr, offset[0]);
        offset[0] += 4;
        if (n == 0) {
            p.children = null;

            StringBuffer sb = new StringBuffer("");
            offset[0] = unserializeString(arr, offset[0], sb);
            p.attr = sb.substring(0);

            p.c = pub.p.getG1().newElement();
            p.cp = pub.p.getG1().newElement();

            offset[0] = unserializeElement(arr, offset[0], p.c);
            offset[0] = unserializeElement(arr, offset[0], p.cp);
        } else {
            p.children = new BswabePolicy[n];
            for (i = 0; i < n; i++)
                p.children[i] = unserializePolicy(pub, arr, offset);
        }

        return p;
    }

    /**
     * 将byte转化int
     *
     * @param b
     *         byte
     *
     * @return int值
     */
    private static int byte2int(byte b) {
        if (b >= 0)
            return b;
        return (256 + b);
    }

    /**
     * 将一个byte数组添加到一个Byte数组的后面
     *
     * @param arrlist
     *         Byte数组
     * @param b
     *         byte数组
     */
    private static void byteArrListAppend(ArrayList<Byte> arrlist, byte[] b) {
        int len = b.length;
        for (int i = 0; i < len; i++)
            arrlist.add(Byte.valueOf(b[i]));
    }

    /**
     * 将Byte数组转化为byte数组
     *
     * @param B
     *         Byte数组
     *
     * @return byte数组
     */
    private static byte[] Byte_arr2byte_arr(ArrayList<Byte> B) {
        int len = B.size();
        byte[] b = new byte[len];

        for (int i = 0; i < len; i++)
            b[i] = B.get(i).byteValue();

        return b;
    }

}
