package com.zhong;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * TSet
 */
public class TSet implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * label
     */
    private String l;
    /**
     * 文件名的密文
     */
    private ArrayList<byte[]> e;
    /**
     * 索引信息
     */
    private SerializableElement y;

    /**
     * 此TSet对应的关键词
     */
    private String keyword;

    public TSet(String l, ArrayList<byte[]> e, SerializableElement y, String keyword) {
        this.l = l;
        this.e = e;
        this.y = y;
        this.keyword = keyword;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getL() {
        return l;
    }

    public ArrayList<byte[]> getE() {
        return e;
    }

    public SerializableElement getY() {
        return y;
    }

    public String getKeyword() {
        return keyword;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===您正在查看TSet的信息===");
        sb.append(System.lineSeparator());
        sb.append("\t" + "标签： " + l);
        sb.append(System.lineSeparator());
        sb.append("\t" + "密文1： " + Arrays.toString(e.get(0)));
        sb.append(System.lineSeparator());
        sb.append("\t" + "密文2: " + Arrays.toString(e.get(1)));
        sb.append(System.lineSeparator());
        sb.append("\t" + "索引： " + y.toString());
        sb.append(System.lineSeparator());
        sb.append("====================================");
        return sb.toString();
    }
}
