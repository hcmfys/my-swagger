package org.springbus.asm;

public class CustomerClassLoader extends  ClassLoader {

    public CustomerClassLoader(){

    }


    public Class defineClass(String className, byte[] toByteArray) {
       return  defineClass(className, toByteArray, 0, toByteArray.length);
    }
}

