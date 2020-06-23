package org.springbus.asm;

public class EmptyClass {
    public int doIt(int a){
        return 0 ;//+sucess();
    }

   // public int sucess(){
        //return 100;
    //}

    public static  void main(String args[]) {
       int a= new EmptyClass().doIt(6);
       System.out.println(a);
    }
}
