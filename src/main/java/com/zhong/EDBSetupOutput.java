package com.zhong;

import java.util.ArrayList;

/**
 * @author 张中俊
 **/
public class EDBSetupOutput {
    /**
     * TSet
     */
    ArrayList<TSet> TSets = new ArrayList<>();
    /**
     * XSet
     */
    ArrayList<String> XSets = new ArrayList<String>();

    public EDBSetupOutput(ArrayList<TSet> TSets, ArrayList<String> XSets) {
        this.TSets = TSets;
        this.XSets = XSets;
    }

    public ArrayList<TSet> getTSets() {
        return TSets;
    }

    public ArrayList<String> getXSets() {
        return XSets;
    }
}
