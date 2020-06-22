package org.springbus.asm;

import lombok.Data;


public class GenClass {

  public GenClass() {

  }

  public GenClass(int a,int c) {

  }

  public int add(int a, int b) {
    return a + b;
  }

  private int  id;


  public static void main(String[] args) {
    int t = new GenClass(3,9).add(4, 6);
    System.out.println(t);
  }
}
