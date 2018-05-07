package com.zhong;

import org.junit.Test;

/**
 * @author 张中俊
 **/
public class MasterKeyTest {
    @Test
    public void generateMasterKeyTest() {
        MasterKey.generateMasterKey();
    }

    @Test
    public void getMasterKeyTest() {
        MasterKey mk = MasterKey.getMasterKey();
        System.out.println(mk);
    }
}
