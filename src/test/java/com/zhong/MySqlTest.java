package com.zhong;

import com.zhong.concurrent.Task;
import com.zhong.rm.MysqlConnPool;
import com.zhong.rm.MysqlHelper;
import org.junit.Test;

import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

/**
 * @author 张中俊
 * @create 2018-04-03 9:59
 **/
public class MySqlTest {
    @Test
    public void t1() throws Exception {
        Map<String, Collection<String>> keyword_filenames = MyUtils.getAToyDB();
        for (String key : keyword_filenames.keySet()) {
            System.out.println(key);
        }
        String keyword = "keyword2";
        Collection<String> filenames = keyword_filenames.get(keyword);

        Task task = EDBSetup.EDBSetup(keyword, filenames);
        System.out.println(task);

        for (TSet tSet : task.gettSets()) {
            System.out.println(tSet.getL().length());
        }
        for (String xSet : task.getxSets()) {
            System.out.println(xSet.length());
        }
        Connection conn = MysqlConnPool.getConnection();
        MysqlHelper.saveTask(task);
    }

    @Test
    public void getTaskTest() {
        Connection conn = MysqlConnPool.getConnection();
        String label = "[57, 80, -85, 72, -52, 89, -10, 51, -81, 57, -65, -63, -98, -87, -73, -25]";
        TSet tSet = MysqlHelper.getTask(label);
        System.out.println(tSet);
    }
}
