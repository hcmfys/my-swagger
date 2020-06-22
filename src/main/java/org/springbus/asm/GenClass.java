package org.springbus.asm;

public class GenClass {
  public GenClass() {

  }

  public GenClass(int a,int c) {

  }

  public int add(int a, int b) {
    return a + b;
  }

  public static void main(String[] args) {
    int t = new GenClass(3,9).add(4, 6);
    System.out.println(t);
  }
}
