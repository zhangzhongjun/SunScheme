package co.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * 公钥
 *
 * @author 张中俊
 */
public class BswabePub {
    public String pairingDesc;
    public Pairing p;
    public Element g;                /* G_1 */
    public Element h;                /* G_1 */
    public Element f;                /* G_1 */
    public Element gp;            /* G_2 */
    public Element g_hat_alpha;    /* G_T */
}
