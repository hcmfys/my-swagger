package org.springbus.adt;

public class Sorter {
    /**
     *
     * @param a
     * @param b
     * @return
     */
    protected   static  boolean  less( int a,int b) {
        return a<b;
    }

    /**
     *
     * @param i
     * @param j
     * @return
     */
    protected   static  boolean  less( int arr[],int i,int j) {
        return arr[i-1] < arr[j-1];
    }
    /**
     * 交换次序
     * @param a
     * @param i
     * @param j
     */
    protected  static  void   exch(int a[],int i,int j){
        int t=a[i];
        a[i]=a[j];
        a[j]=t;
    }

    /**
     *
     * @param a
     * @param i
     * @param j
     */
    protected  static  void   exch(Comparable[] a,int i,int j){
        Comparable t=a[i];
        a[i]=a[j];
        a[j]=t;
    }
}
