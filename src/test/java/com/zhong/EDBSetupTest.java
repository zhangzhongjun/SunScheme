package com.zhong;

import com.zhong.concurrent.Task;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author 张中俊
 * @create 2018-04-04 20:33
 **/
public class EDBSetupTest {
    @Test
    public void setupTest() throws Exception {
        String keyword = "key1";
        ArrayList<String> filenames = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            filenames.add("ind" + i);
        }
        Task task = EDBSetup.EDBSetup(keyword, filenames);
        System.out.println(task);
    }
}
