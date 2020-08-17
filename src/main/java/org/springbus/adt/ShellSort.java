package org.springbus.adt;

//希尔排序听名字就能想到是Shell提出来的，只是对直接插入排序做了一个基本的改进。什么改进呢？
//
//希尔排序是把序列按一定间隔分组，对每组使用直接插入排序；随着间隔减小，一直到1，使得整个序列有序
//最佳情况：T(n) = O(nlogn)。最坏情况：T(n) = O(n)。平均情况：T(n) = O(nlogn)。
//时间复杂度， 具体取决于间隔 h，最好：O(nlogn)、最坏：O(n^2)、平均：无。
//希尔算法的性能与h有很大关系。只对特定的待排序记录序列，
//可以准确地估算关键词的比较次数和对象移动次数。想要弄清关键词比较次数和记录移动次数与h选择之间的关系，
//并给出完整的数学分析，至今仍然是数学难题。
//空间复杂度， O(1)
//稳定性，一次插入排序是稳定的，但在不同的插入排序过程中，相同的元素可能在各自的插入排序中移动，
//最后其稳定性就会被打乱，shell排序每个不同的增量都是插入排序，
//有多次，实际上是分组插入排序（又叫缩小增量排序），所以是不稳定的
public class ShellSort extends  Sorter {


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

        // 3x+1 increment sequence:  1, 4, 13, 40, 121, 364, 1093, ...
        int h = 1;
        while (h < n/3) h = 3*h + 1;

        while (h >= 1) {
            // h-sort the array
            for (int i = h; i < n; i++) {
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h) {
                    exch(a, j, j-h);
                }
            }
            h /= 3;
        }
    }


}
