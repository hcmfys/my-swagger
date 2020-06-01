package org.springbus.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadGenList {

  static void p(String m) {
    System.out.println("[" + Thread.currentThread().getName() + "] ==>" + m);
  }

  static void test1() {
    List<String> o = new ArrayList<>();
    List<String> r = new ArrayList<>();
    AtomicInteger index = new AtomicInteger();

    Thread t =
        new Thread(
            () -> {
              p(" start run");

              try {
                synchronized (o) {
                  for (; ; ) {
                    p(" get o1  lock ");
                    if (o.size() == 0) {
                      p(" get o1  lock 0  size wait");
                      // o.notifyAll();
                      o.wait();

                    } else {
                      p(" start to get  o1  lock 1 ");
                      for (int i = 0; i < o.size(); i++) {
                        p(o.get(i) + " find ....");
                      }
                      index.decrementAndGet();
                      o.clear();

                      // o.notifyAll();
                      o.notify();
                      o.wait();
                    }
                  }
                }

              } catch (Exception e) {
                e.printStackTrace();
              }
            });
    t.setName("t1");
    t.start();

    Thread t3 =
        new Thread(
            () -> {
              try {
                p(" get oooooo lock ");

                synchronized (o) {
                  p(" get 0ooooo0 lock add one ");
                  for (; ; ) {
                    o.add((index.incrementAndGet()) + "-->");
                      p(" get 0ooooo0 lock add one    finish ");
                    o.notifyAll();
                      p(" get 0ooooo0 lock add one and notify ");
                   o.wait();
                  }
                }

              } catch (Exception e) {
                e.printStackTrace();
              }
            });
    t3.setName("t3");
    t3.start();
  }

  public static void main(String[] args) {

    test1();
  }
}
