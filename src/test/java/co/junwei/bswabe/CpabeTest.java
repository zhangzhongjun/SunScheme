package co.junwei.bswabe;

import co.junwei.cpabe.Common;
import co.junwei.cpabe.Cpabe;
import com.zhong.MyUtils;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * @author 张中俊
 **/
public class CpabeTest {
    @Test
    public void setupTest() throws IOException, ClassNotFoundException {
        String pubfile = MyUtils.getFile("abe_keys", "pub_key").getAbsolutePath();
        String mskfile = MyUtils.getFile("abe_keys", "master_key").getAbsolutePath();
        Cpabe cpabe = new Cpabe();
        cpabe.setup(pubfile, mskfile);
    }

    @Test
    public void keygenTest() throws IOException, NoSuchAlgorithmException {
        String pubfile = MyUtils.getFile("abe_keys", "pub_key").getAbsolutePath();
        String mskfile = MyUtils.getFile("abe_keys", "master_key").getAbsolutePath();
        String prvfile = MyUtils.getFile("abe_keys", "prv_key").getAbsolutePath();
        //属性列表
        String student_attr = "objectClass:inetOrgPerson objectClass:organizationalPerson "
                + "sn:student2 cn:student2 uid:student2 userPassword:student2 "
                + "ou:idp o:computer mail:student2@sdu.edu.cn title:student";
        Cpabe cpabe = new Cpabe();
        cpabe.keygen(pubfile, prvfile, mskfile, student_attr);
    }

    @Test
    public void encDecTest() throws Exception {
        String pubfile = MyUtils.getFile("abe_keys", "pub_key").getAbsolutePath();
        String prvfile = MyUtils.getFile("abe_keys", "prv_key").getAbsolutePath();
        String policy = "sn:student2 cn:student2 uid:student2 3of3";

        Cpabe cpabe = new Cpabe();
        ArrayList<byte[]> res = cpabe.enc(pubfile, policy, "hello world".getBytes("utf-8"));
        System.out.println((cpabe.dec(pubfile, prvfile, res)));

        res = cpabe.enc(pubfile, policy, "java sse implement".getBytes("utf-8"));
        System.out.println((cpabe.dec(pubfile, prvfile, res)));
    }

    @Test
    public void timeTest() throws Exception {
        String pubfile = MyUtils.getFile("abe_keys", "pub_key").getAbsolutePath();
        String prvfile = MyUtils.getFile("abe_keys", "prv_key").getAbsolutePath();
        String policy = "sn:student2 cn:student2 uid:student2 3of3";

        Long t1 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Cpabe cpabe = new Cpabe();
            ArrayList<byte[]> res = cpabe.enc(pubfile, policy, "hello world".getBytes("utf-8"));
            System.out.println((cpabe.dec(pubfile, prvfile, res)));
        }
        Long t2 = System.currentTimeMillis();
        System.out.println("100次加解需要花费 " + (t2 - t1) + " ms");

        /* get BswabePub from pubfile */
        byte[] pub_byte = Common.suckFile(pubfile);
        BswabePub pub = SerializeUtils.unserializeBswabePub(pub_byte);
        /* get BswabePrv form prvfile */
        byte[] prv_byte = Common.suckFile(prvfile);
        BswabePrv prv = SerializeUtils.unserializeBswabePrv(pub, prv_byte);

        t1 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Cpabe cpabe = new Cpabe();
            ArrayList<byte[]> res = cpabe.enc(pub, policy, "hello world".getBytes("utf-8"));
            System.out.println((cpabe.dec(pub, prv, res)));
        }
        t2 = System.currentTimeMillis();
        System.out.println("100次加解需要花费 " + (t2 - t1) + " ms");
    }
}
