package org.springbus.adt;

//堆排序
//原理：利用堆这种数据结构的一种排序算法，
//堆是一个近似完全二叉树的结构，满足堆的性质：即子结点的键总是小于（或者大于）它的父节点
//具体操作：将初始待排序关键字序列(R1,R2....Rn)构建成大顶堆，
//此堆为初始的无序区； 将堆顶元素R[1]与最后一个元素R[n]交换，
//此时得到新的无序区(R1,R2,......Rn-1)和新的有序区(Rn),且满足R[1,2...n-1]<=R[n]；
//由于交换后新的堆顶R[1]可能违反堆的性质，因此需要对当前无序区(R1,R2,......Rn-1)调整为新堆，
//然后再次将R[1]与无序区最后一个元素交换，
//得到新的无序区(R1,R2....Rn-2)和新的有序区(Rn-1,Rn)。
//不断重复此过程直到有序区的元素个数为n-1，则整个排序过程完成。
//时间复杂度， 都是：O(nlogn)
//空间复杂度， O(1)
//稳定性，比如：3 27 36 27，堆顶3先输出，则第三层的27（最后一个27）跑到堆顶，
//然后堆稳定，继续输出堆顶，是刚才那个27，这样说明后面的27先于第二个位置的27输出，不稳定

public class HeapSort extends  Sorter {


    public  static  void main(String[] args) {
        int [] arr={ 8,7,10,12,1,2,6,6,90,444,90,4493,393};
        HeapSort(arr);
        for(int a: arr) {
            System.out.print(a + " ");
        }
        System.out.println("");
    }


    public   static  void  HeapSort(int [] pg){
        int n=pg.length;
        for(int k=n/2;k>=1; k--){
            sink(pg, k, n);
        }
        while (n>1) {
            exch(pg, 1, n--);
            sink(pg, 1, n);
        }

    }


    private  static  void sink(int[] pg,int k,int n) {
        while(2*k <=n) {
            int j=2*k;
            if(j<n && less(pg,j,j+1)) j++; //先比较左右子节点，找到较大的
            if(!less(pg, k,j)) break;//大于较大的子节点，无需下沉
            exch(pg, k, j);//否则下沉
            k=j;//继续比较以这个节点为根的子树
        }
    }
}
