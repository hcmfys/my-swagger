package org.springbus.thread;

public class ThreadApp {

  static void test1() {
    final Object o = new Object();

    Thread t =
        new Thread(
            () -> {
              System.out.println(Thread.currentThread().getName() + " start run");

              try {
                synchronized (o) {
                  o.wait();
                  System.out.println(Thread.currentThread().getName() + " run again   one");
                  o.wait(1);
                  System.out.println(Thread.currentThread().getName() + " run again  200 ");
                  o.wait(1);
                }
                System.out.println(Thread.currentThread().getName() + " run again  end");
              } catch (Exception e) {
                e.printStackTrace();
              }
            });
    t.setName(" t name ");
    t.start();

    Thread t3 =
        new Thread(
            () -> {
              System.out.println(Thread.currentThread().getName() + " start run");

              try {
                synchronized (o) {
                    System.out.println(Thread.currentThread().getName() + " start  notify all");
                  o.notifyAll();
                  System.out.println(Thread.currentThread().getName() + " start  wait 0 run");
                  o.wait(1);
                  System.out.println(Thread.currentThread().getName() + " start run  1 ");
                    o.wait(1);
                    System.out.println(Thread.currentThread().getName() + " start run 2");
                }

              } catch (Exception e) {
                e.printStackTrace();
              }
            });
    t3.setName(" t3 name");
    t3.start();
  }

  public static void main(String[] args) {

    test1();
  }
}
