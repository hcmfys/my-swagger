package org.springbus.adt;


//快速排序
//原理：通过一趟排序将待排记录分隔成独立的两部分，
//其中一部分记录的关键字均比另一部分的关键字小，然后分别对这两部分记录进行排序，以达到整个序列有序
//具体操作：从数列中挑出一个元素，称为 "基准"（pivot）；
//重新排序数列，所有元素比基准值小的摆放在基准前面，
//所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。
//在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
//递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。
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
