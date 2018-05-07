package co.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Test;

/**
 * @author 张中俊
 **/
public class SerializeUtilsTest {
    @Test
    public void ElementSizeTest() {
        final Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
        Element e1 = pairing.getG1().newRandomElement();
        System.out.println(e1.toBytes().length + " byte");
        Element e2 = pairing.getG2().newRandomElement();
        System.out.println(e2.toBytes().length + " byte");
        Element e3 = pairing.getGT().newRandomElement();
        System.out.println(e3.toBytes().length + " byte");
        Element e4 = pairing.getZr().newRandomElement();
        System.out.println(e4.toBytes().length + " byte");
    }

    @Test
    public void ElementSizeTest2() {
        final Pairing pairing = PairingFactory.getPairing("params/curves/a1.properties");
        Element e1 = pairing.getG1().newRandomElement();
        System.out.println(e1.toBytes().length + " byte");
        Element e2 = pairing.getG2().newRandomElement();
        System.out.println(e2.toBytes().length + " byte");
        Element e3 = pairing.getGT().newRandomElement();
        System.out.println(e3.toBytes().length + " byte");
        Element e4 = pairing.getZr().newRandomElement();
        System.out.println(e4.toBytes().length + " byte");
    }

    @Test
    public void ElementSizeTest3() {
        final Pairing pairing = PairingFactory.getPairing("params/curves/d159.properties");
        Element e1 = pairing.getG1().newRandomElement();
        System.out.println(e1.toBytes().length + " byte");
        Element e2 = pairing.getG2().newRandomElement();
        System.out.println(e2.toBytes().length + " byte");
        Element e3 = pairing.getGT().newRandomElement();
        System.out.println(e3.toBytes().length + " byte");
        Element e4 = pairing.getZr().newRandomElement();
        System.out.println(e4.toBytes().length + " byte");
    }

    @Test
    public void ElementSizeTest4() {
        final Pairing pairing = PairingFactory.getPairing("params/curves/d201.properties");
        Element e1 = pairing.getG1().newRandomElement();
        System.out.println(e1.toBytes().length + " byte");
        Element e2 = pairing.getG2().newRandomElement();
        System.out.println(e2.toBytes().length + " byte");
        Element e3 = pairing.getGT().newRandomElement();
        System.out.println(e3.toBytes().length + " byte");
        Element e4 = pairing.getZr().newRandomElement();
        System.out.println(e4.toBytes().length + " byte");
    }

    @Test
    public void ElementSizeTest5() {
        final Pairing pairing = PairingFactory.getPairing("params/curves/d224.properties");
        Element e1 = pairing.getG1().newRandomElement();
        System.out.println(e1.toBytes().length + " byte");
        Element e2 = pairing.getG2().newRandomElement();
        System.out.println(e2.toBytes().length + " byte");
        Element e3 = pairing.getGT().newRandomElement();
        System.out.println(e3.toBytes().length + " byte");
        Element e4 = pairing.getZr().newRandomElement();
        System.out.println(e4.toBytes().length + " byte");
    }

    @Test
    public void ElementSizeTest6() {
        final Pairing pairing = PairingFactory.getPairing("params/curves/e.properties");
        Element e1 = pairing.getG1().newRandomElement();
        System.out.println(e1.toBytes().length + " byte");
        Element e2 = pairing.getG2().newRandomElement();
        System.out.println(e2.toBytes().length + " byte");
        Element e3 = pairing.getGT().newRandomElement();
        System.out.println(e3.toBytes().length + " byte");
        Element e4 = pairing.getZr().newRandomElement();
        System.out.println(e4.toBytes().length + " byte");
    }


    @Test
    public void ElementSizeTest7() {
        final Pairing pairing = PairingFactory.getPairing("params/curves/f.properties");
        Element e1 = pairing.getG1().newRandomElement();
        System.out.println(e1.toBytes().length + " byte");
        Element e2 = pairing.getG2().newRandomElement();
        System.out.println(e2.toBytes().length + " byte");
        Element e3 = pairing.getGT().newRandomElement();
        System.out.println(e3.toBytes().length + " byte");
        Element e4 = pairing.getZr().newRandomElement();
        System.out.println(e4.toBytes().length + " byte");
    }

    @Test
    public void ElementSizeTest8() {
        final Pairing pairing = PairingFactory.getPairing("params/curves/g149.properties");
        Element e1 = pairing.getG1().newRandomElement();
        System.out.println(e1.toBytes().length + " byte");
        Element e2 = pairing.getG2().newRandomElement();
        System.out.println(e2.toBytes().length + " byte");
        Element e3 = pairing.getGT().newRandomElement();
        System.out.println(e3.toBytes().length + " byte");
        Element e4 = pairing.getZr().newRandomElement();
        System.out.println(e4.toBytes().length + " byte");
    }
}
