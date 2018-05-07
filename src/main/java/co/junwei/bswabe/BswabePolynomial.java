package co.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;

/**
 * 多项式
 *
 * @author 张中俊
 */
public class BswabePolynomial {
    int deg;
    /* coefficients from [0] x^0 to [deg] x^deg */
    Element[] coef; /* G_T (of length deg+1) */
}
