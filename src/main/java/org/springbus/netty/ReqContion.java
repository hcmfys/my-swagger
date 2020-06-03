package org.springbus.netty;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReqContion {
    private  volatile int req;
    private  static  byte[] b=new byte[0];
    Lock lock=new ReentrantLock();

    public    int getReq2() {
        lock.lock();
        try {
            if (req > 999) {
                req = 0;
            } else {
                req++;
            }
            return req;
        }finally {
            lock.unlock();
        }
    }

    public    int getReq() {

        synchronized (ReqContion.class){
            if (req > 999) {
                req = 0;
            } else {
                req++;
            }
            return req;
        }

    }

    static  void test(){
        ReqContion rq=new ReqContion();
        Map<Integer,Integer> map=new HashMap<>();
        for(int i=0;i<20;i++) {
            Thread t=new Thread(() -> {
                for(int j=0;j<20;j++) {
                    int q = rq.getReq();
                    //log.info("thread name= {}  q ={}", Thread.currentThread().getName(), q);
                    if (map.get(q) == null) {
                        map.put(q, q);
                    } else {
                        log.info("======= exits========={}", q);
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.setName("t"+(i+1));
            t.start();
        }
    }

    public   static  void main(String[] args ) throws InterruptedException {
        for (int i = 0; i < 1000; i++)
            test();

    }

}
