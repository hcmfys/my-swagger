package org.springbus.asm;

import java.io.IOException;
import java.lang.reflect.Proxy;

public class TestStudent {

    public static void main(String[] args) throws IOException {
        Student s = new Student();
        MyInvokerProxy invokerProxy = new MyInvokerProxy(s);
        IStudent student = (IStudent) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{IStudent.class}, invokerProxy);
        student.display("javax");


    }
}
