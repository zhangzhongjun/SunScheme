package com.zhong.concurrent;

import com.zhong.TSet;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 我们将Task作为生产和消费的单位：
 *
 * @author 张中俊
 **/
public class Task {
    /**
     * 密文 + 索引y
     */
    private ArrayList<TSet> tSets;
    /**
     * 该关键词对应的所有的XSet
     */
    private ArrayList<String> xSets;

    public Task(ArrayList<TSet> tSets, ArrayList<String> xSets) {
        this.tSets = tSets;
        this.xSets = xSets;
    }

    public ArrayList<TSet> gettSets() {
        return tSets;
    }

    public ArrayList<String> getxSets() {
        return xSets;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("============你现在在查看Task中的内容============");
        sb.append(System.lineSeparator());
        sb.append("TSets:");
        sb.append(System.lineSeparator());
        int i = 0;
        for (TSet tSet : tSets) {
            sb.append("第" + i + "条TSet：");
            sb.append(System.lineSeparator());
            sb.append("\t标签： " + tSet.getL() + " ");
            sb.append(System.lineSeparator());
            sb.append("\t密文1： " + Arrays.toString(tSet.getE().get(0)) + " ");
            sb.append(System.lineSeparator());
            sb.append("\t密文2： " + Arrays.toString(tSet.getE().get(1)) + " ");
            sb.append(System.lineSeparator());
            sb.append("\t索引： " + tSet.getY().getElement().toString());
            sb.append(System.lineSeparator());
            i++;
        }
        sb.append(System.lineSeparator());
        sb.append("XSets:");
        sb.append(System.lineSeparator());
        i = 0;
        for (String xSet : xSets) {
            sb.append("第" + i + "条XSet：");
            sb.append(xSet);
            sb.append(System.lineSeparator());
            i++;
        }
        return sb.toString();
    }


}
