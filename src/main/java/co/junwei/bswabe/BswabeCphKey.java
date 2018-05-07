package co.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;

/**
 * 有一些函数同时返回密文和密钥
 *
 * @author 张中俊
 */
public class BswabeCphKey {
    /*
     * This class is defined for some classes who return both cph and key.
     */
    public BswabeCph cph;
    public Element key;
}
