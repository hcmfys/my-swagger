package org.springbus.asm;

import org.springframework.beans.factory.annotation.Value;

import javax.management.DescriptorKey;

public class Test {


    @Value(value ="测试")
    public  void doIt(){

    }
    public static void main(String[] args) {
        Test  t=new Test() ;
        t.doIt();
        System.out.println(t);
    }
}
