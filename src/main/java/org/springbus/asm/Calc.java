package org.springbus.asm;

public class Calc {

    private int add(int a, int b) {

        return (a + b);
    }





    public   int cal( ) {
        int a = 100;
        int b = 200;
        int c = 300;
        return (a + b) * c;
    }

    private  int ifCal( ) {
        int a = 100;
        int b = 200;
        int c = 300;
        if (a > b) {
            return a;
        }
        return b;
    }
}
