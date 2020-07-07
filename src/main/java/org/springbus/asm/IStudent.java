package org.springbus.asm;

public interface IStudent {
   default void display(){
       System.out.println("display none");
   }
    void display(String name);
}
