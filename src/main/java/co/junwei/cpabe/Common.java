package co.junwei.cpabe;

import java.io.*;

/**
 * 公用的类
 *
 * @author 张中俊
 */
public class Common {

    /**
     * read byte[] from inputfile
     * 从一个文件中读出byte数组
     *
     * @param inputfile
     *         要读取的文件
     *
     * @return 读取出的数组
     *
     * @throws IOException
     *         异常
     */
    public static byte[] suckFile(String inputfile) throws IOException {
        InputStream is = new FileInputStream(inputfile);
        int size = is.available();
        byte[] content = new byte[size];

        is.read(content);

        is.close();
        return content;
    }

    /**
     * write byte[] into outputfile
     * 将byte数组输出到文件中
     *
     * @param outputfile
     *         要输出到哪个文件
     * @param b
     *         要输出的byte数组
     *
     * @throws IOException
     *         异常
     */
    public static void spitFile(String outputfile, byte[] b) throws IOException {
        OutputStream os = new FileOutputStream(outputfile);
        os.write(b);
        os.close();
    }


    /**
     * 将密文序列化到文件中
     *
     * @param encfile
     *         要输出到哪个文件
     * @param cphBuf
     *         密文的第一部分
     * @param aesBuf
     *         密文的第二部分
     *
     * @throws IOException
     *         异常
     */
    public static void writeCpabeFile(String encfile,
                                      byte[] cphBuf, byte[] aesBuf) throws IOException {
        int i;
        OutputStream os = new FileOutputStream(encfile);

        /* write aes_buf */
        for (i = 3; i >= 0; i--)
            os.write(((aesBuf.length & (0xff << 8 * i)) >> 8 * i));
        os.write(aesBuf);

        /* write cph_buf */
        for (i = 3; i >= 0; i--)
            os.write(((cphBuf.length & (0xff << 8 * i)) >> 8 * i));
        os.write(cphBuf);

        os.close();

    }

    /**
     * 读取存储在文件中的密文
     *
     * @param encfile
     *         存储加密结果的文件
     *
     * @return 读取出的密文
     *
     * @throws IOException
     *         异常
     */
    public static byte[][] readCpabeFile(String encfile) throws IOException {
        int i, len;
        InputStream is = new FileInputStream(encfile);
        byte[][] res = new byte[2][];
        byte[] aesBuf, cphBuf;

        /* read aes buf */
        len = 0;
        for (i = 3; i >= 0; i--)
            len |= is.read() << (i * 8);
        aesBuf = new byte[len];

        is.read(aesBuf);

        /* read cph buf */
        len = 0;
        for (i = 3; i >= 0; i--)
            len |= is.read() << (i * 8);
        cphBuf = new byte[len];

        is.read(cphBuf);

        is.close();

        res[0] = aesBuf;
        res[1] = cphBuf;
        return res;
    }

    /**
     * Return a ByteArrayOutputStream instead of writing to a file
     * <p>
     * 将密文以输出流的形式返回
     *
     * @param mBuf
     *         密文的第一部分
     * @param cphBuf
     *         密文的第二部分
     * @param aesBuf
     *         密文的第三部分
     *
     * @return 输出流
     *
     * @throws IOException
     *         异常
     */
    public static ByteArrayOutputStream writeCpabeData(byte[] mBuf,
                                                       byte[] cphBuf, byte[] aesBuf) throws IOException {
        int i;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        /* write m_buf */
        for (i = 3; i >= 0; i--)
            os.write(((mBuf.length & (0xff << 8 * i)) >> 8 * i));
        os.write(mBuf);

        /* write aes_buf */
        for (i = 3; i >= 0; i--)
            os.write(((aesBuf.length & (0xff << 8 * i)) >> 8 * i));
        os.write(aesBuf);

        /* write cph_buf */
        for (i = 3; i >= 0; i--)
            os.write(((cphBuf.length & (0xff << 8 * i)) >> 8 * i));
        os.write(cphBuf);

        os.close();
        return os;
    }

    /**
     * Read data from an InputStream instead of taking it from a file.
     * 从一个输入流中获取密文数据
     *
     * @param is
     *         输入流
     *
     * @return 读取到的密文
     *
     * @throws IOException
     *         异常
     */
    public static byte[][] readCpabeData(InputStream is) throws IOException {
        int i, len;

        byte[][] res = new byte[3][];
        byte[] mBuf, aesBuf, cphBuf;

        /* read m buf */
        len = 0;
        for (i = 3; i >= 0; i--)
            len |= is.read() << (i * 8);
        mBuf = new byte[len];
        is.read(mBuf);
        /* read aes buf */
        len = 0;
        for (i = 3; i >= 0; i--)
            len |= is.read() << (i * 8);
        aesBuf = new byte[len];
        is.read(aesBuf);

        /* read cph buf */
        len = 0;
        for (i = 3; i >= 0; i--)
            len |= is.read() << (i * 8);
        cphBuf = new byte[len];
        is.read(cphBuf);

        is.close();
        res[0] = aesBuf;
        res[1] = cphBuf;
        res[2] = mBuf;
        return res;
    }
}
