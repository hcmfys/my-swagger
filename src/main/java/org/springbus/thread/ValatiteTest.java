package org.springbus.thread;

import sun.font.Type1GlyphMapper;

public class ValatiteTest {

    private    boolean stopIt=false;

    public     void test(){
        Thread t=new Thread(()->{

            while(!stopIt) {
                System.out.println( Thread.currentThread().getName()+ " i runing ....");
            }
        });
        t.setName("t1");

        Thread t2=new Thread(()->{
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             stopIt=true;
        });
        t2.setName("t2");

        t.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();

    }



    public  static  void main(String[] args) {
       new ValatiteTest(). test();
    }
}
