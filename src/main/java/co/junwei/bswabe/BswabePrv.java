package co.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

/**
 * 私钥
 *
 * @author 张中俊
 */
public class BswabePrv {
    /*
     * A private key
     */
    Element d; /* G_2 */
    ArrayList<BswabePrvComp> comps; /* BswabePrvComp */
}