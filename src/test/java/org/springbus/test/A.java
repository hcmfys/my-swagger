package org.springbus.test;

import lombok.Data;
import org.jooq.meta.derby.sys.Sys;

@Data
public class A {

    private String da;
    private int shao;

    void test(){
        String a[]=new String[2];
        Object t[]=a;
        a[0]="java";
        t[1]=Integer.valueOf(1233);
       // System.out.println(t);
    }

    public  static  void main(String [] args) {
        new A().test();
    }


}
