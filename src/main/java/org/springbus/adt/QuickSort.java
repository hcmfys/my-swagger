package org.springbus.adt;

public class QuickSort {


    public  static  void main(String[] args) {
        int [] arr={ 8,7,10,12,1,2,6,6,90,444,90,4493,393};
         quickSort(arr);
         for(int a: arr) {
             System.out.print(a + " ");
         }
        System.out.println("");
    }

    /**
     *
     * @param arr
     */
    public   static  void  quickSort(int [] arr){
        sort(arr,0, arr.length-1);
    }

    public static  void  sort(int a[],int lo,int hi){
        if(hi<=lo) return;
        int j=partition(a, lo, hi);
        sort(a,lo, j-1);
        sort(a,j+1,hi);
    }

    /**
     * // partition the subarray a[lo..hi] so that a[lo..j-1] <= a[j] <= a[j+1..hi]
     * // and return the index j.
     * @param a
     * @param lo
     * @param hi
     * @return
     */
    private static  int  partition(int[] a, int lo, int hi) {
        int i=lo;
        int j=hi+1;
        int v=a[lo];
        for (;;) {
            while( a[++i] <v ) {
                if (i == hi) break;
            }
            while( a[--j]> v ) {
                if (j == lo) break;
            }
            if(i>=j) break;
            exch(a, i, j);
        }
        exch(a, lo, j);
        return  j;
    }

    /**
     *
     * @param i
     * @param j
     * @return
     */
    private  static  boolean  less( int i,int j) {
        return i<j;
    }
    /**
     * 交换次序
     * @param a
     * @param i
     * @param j
     */
    private  static  void   exch(int a[],int i,int j){
        int t=a[i];
        a[i]=a[j];
        a[j]=t;
    }
}
