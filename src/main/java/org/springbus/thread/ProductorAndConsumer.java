package org.springbus.thread;

import lombok.SneakyThrows;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
import org.jooq.meta.derby.sys.Sys;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProductorAndConsumer {

  private static Lock lock = new ReentrantLock();

  static void test1() {

    Condition r = lock.newCondition();
    Condition w = lock.newCondition();
    Stack<Long> lst = new Stack<>();
    final long[] nums = {0};

    Thread t1 =
        new Thread(
            () -> {
              for (; ; ) {
                try {
                  lock.lock();
                  while (lst.size() > 0) {
                    long l = lst.pop();
                    System.out.println(Thread.currentThread().getName() + " get " + l);
                  }
                  w.signal();
                  r.await();

                } catch (Exception ex) {
                  ex.printStackTrace();
                } finally {
                  lock.unlock();
                }
              }
            },
            "t1");

    Thread t2 =
        new Thread(
            () -> {
              for (; ; ) {
                try {
                  lock.lock();

                  nums[0]++;
                  lst.add(nums[0]);
                  System.out.println(Thread.currentThread().getName() + " add " + nums[0]);
                  r.signal();
                  Thread.sleep(1000);
                  w.await();

                } catch (Exception e) {
                  e.printStackTrace();
                } finally {
                  lock.lock();
                }
              }
            },
            "t2");
    t2.start();
    t1.start();
  }

  public static void main(String[] args) {

    test1();
  }
}
