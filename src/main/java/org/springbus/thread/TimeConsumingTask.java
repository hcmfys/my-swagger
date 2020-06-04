package org.springbus.thread;

public class TimeConsumingTask implements  Runnable{

    private  volatile boolean  toCancel=false;

    @Override
    public void run() {
        while (!toCancel) {
            doExecute();
        }
        if(toCancel) {
            System.out.println("task was canceled!");
        }else{
            System.out.println("task done!");
        }
    }

    private  boolean doExecute() {

        boolean isDone=false;
        System.out.println(" executing ...");

       // Tools.randomPause(500);
        return isDone;

    }

    public void cancel() {
        toCancel=true;
    }

    public  static  void main(String[] args) {

        for(int i=0;i<1000;i++) {
            TimeConsumingTask task = new TimeConsumingTask();
            Thread t = new Thread(task);
            t.start();
            Tools.randomPause(2);
            task.cancel();
        }
    }




}
