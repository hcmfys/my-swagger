package org.springbus.adt;

//插入排序
//原理：将未排序的序列中的每一个数据依次按合理的顺序插入已排列的数据中。
//具体操作：构建有序序列，对于未排序数据，在已排序序列中从头扫描，找到相应位置并插入。
//第一趟第一个就是有序数据，第二趟把第二个数据和第一个有序数据排序，
//第三趟把第三个数据和一、二个有序数据排序，以此类推直至排序完毕
//时间复杂度， 都是：O(n^2)
//空间复杂度， O(1)
//稳定性，不稳定
public class InsertSort extends  Sorter {

    public  static  void main(String[] args) {
        int [] arr={ 8,7,10,12,1,2,6,6,90,444,90,4493,393};
        sort(arr);
        for(int a: arr) {
            System.out.print(a + " ");
        }
        System.out.println("");
    }


    public static void sort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = i; j > 0 && less(a[j], a[j-1] ); j--) {
                exch(a, j, j-1);//将未排序的第一个数据插入已排序的数据汇中的合适位置
            }
        }
    }
}
