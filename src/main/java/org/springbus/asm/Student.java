package org.springbus.asm;

import org.jooq.meta.derby.sys.Sys;


public class Student  implements  IStudent {
    @Override
    public void display(String name) {
        System.out.println("hi "+name);
    }
}
