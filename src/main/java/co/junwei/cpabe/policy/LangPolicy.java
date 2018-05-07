package co.junwei.cpabe.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

/**
 * 策略类
 */
public class LangPolicy {

    /**
     * 解析属性
     *
     * @param s
     *         含有属性的字符串
     *
     * @return 解析好的字符串
     */
    public static String[] parseAttribute(String s) {
        ArrayList<String> str_arr = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(s);
        String token;
        String res[];
        int len;

        while (st.hasMoreTokens()) {
            token = st.nextToken();
            if (token.contains(":")) {
                str_arr.add(token);
            } else {
                System.out.println("Some error happens in the input attribute");
                System.exit(0);
            }
        }

        Collections.sort(str_arr, new SortByAlphabetic());

        len = str_arr.size();
        res = new String[len];
        for (int i = 0; i < len; i++)
            res[i] = str_arr.get(i);
        return res;
    }

    public static void main(String[] args) {
        String attr = "objectClass:inetOrgPerson objectClass:organizationalPerson "
                + "sn:student2 cn:student2 uid:student2 userPassword:student2 "
                + "ou:idp o:computer mail:student2@sdu.edu.cn title:student";
        String[] arr = parseAttribute(attr);
        for (int i = 0; i < arr.length; i++)
            System.out.println(arr[i]);
    }

    /**
     * 字母序排序
     */
    static class SortByAlphabetic implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            if (s1.compareTo(s2) >= 0)
                return 1;
            return 0;
        }

    }
}
