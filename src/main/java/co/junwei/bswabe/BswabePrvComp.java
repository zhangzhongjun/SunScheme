package co.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;

/**
 * 私钥？
 *
 * @author 张中俊
 */
public class BswabePrvComp {
    /* these actually get serialized */
    String attr;
    Element d;                    /* G_2 */
    Element dp;                /* G_2 */

    /* only used during dec */
    int used;
    Element z;                    /* G_1 */
    Element zp;                /* G_1 */
}
