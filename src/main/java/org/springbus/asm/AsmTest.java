package org.springbus.asm;

import  org.objectweb.asm.util.ASMifier;

public class AsmTest {

    public  static  void main(String[] args) throws Exception {
        ASMifier.main(new String[] {"-debug" ,"org.springbus.asm.Calc" });
    }
}
