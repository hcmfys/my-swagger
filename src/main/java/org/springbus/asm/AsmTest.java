package org.springbus.asm;

import jdk.internal.org.objectweb.asm.util.ASMifier;

public class AsmTest {

    public  static  void main(String[] args) throws Exception {
        ASMifier.main(new String[] {"-debug" ,"org.springbus.asm.EmptyClass" });
    }
}
