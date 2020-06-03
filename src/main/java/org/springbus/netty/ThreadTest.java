package org.springbus.netty;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadTest {

    static class TaskJob implements  Runnable {
        private  int job;
        public  TaskJob(int job) {
            this.job=job;
        }

        @Override
        public void run() {
            log.info(" thread name={} start job ={} ", Thread.currentThread().getName(), job);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static  ThreadFactory getThreadFactory(){
        ThreadFactory t=new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t=new Thread(r,"my-app-thread");

                return t;
            }
        };
        return  t;
    }
    private  static  void  testDefaultEventExecutor(){
        long t1=System.currentTimeMillis();
        DefaultEventExecutor defaultEventExecutor =new DefaultEventExecutor(getThreadFactory());
         for(int i=0;i<100;i++){
             defaultEventExecutor.submit(new TaskJob(i+1));
         }
        long t2=System.currentTimeMillis();
        log.info("=============all cost time=", t2-t1);
    }


    private  static  void  testDefaultEventExecutorGroup(){
        long t1=System.currentTimeMillis();
        DefaultEventExecutorGroup   defaultEventExecutorGroup =new DefaultEventExecutorGroup(5);
        for(int i=0;i<100;i++){
            defaultEventExecutorGroup.submit(new TaskJob(i+1));
        }
        defaultEventExecutorGroup.schedule(new TaskJob(999), 15, TimeUnit.SECONDS);

        defaultEventExecutorGroup.scheduleAtFixedRate(new TaskJob(1099), 15,1, TimeUnit.SECONDS);
        long t2=System.currentTimeMillis();
        log.info("============all cost time=", t2-t1);
    }




    public   static  void main(String[] args ) throws InterruptedException {
        //testDefaultEventExecutor();
        testDefaultEventExecutorGroup();
    }

}
