package org.springbus.thread;

public class Tools {
    public  static  void randomPause(long times)    {
        try {
            Thread.sleep(times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
