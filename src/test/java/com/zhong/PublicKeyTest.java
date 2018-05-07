package com.zhong;

import org.junit.Test;

/**
 * @author 张中俊
 **/
public class PublicKeyTest {
    @Test
    public void generatePublicKeyTest() {
        PublicKey.generatePublicKey();
    }

    @Test
    public void getPublicKeyTest() {
        PublicKey pk = PublicKey.getPublicKey();
        System.out.println(pk);
    }
}
