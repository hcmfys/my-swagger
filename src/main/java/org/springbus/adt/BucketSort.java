package org.springbus.adt;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 桶排序
 * 原理：假设输入数据服从均匀分布，将数据分到有限数量的桶里，
 * 每个桶再分别排序（有可能再使用别的排序算法或是以递归方式继续使用桶排序进行排）
 * 具体操作： 设置一个定量的数组当作空桶；遍历输入数据，并且把数据一个一个放到对应的桶里去；
 * 对每个不是空的桶进行排序；从不是空的桶里把排好序的数据拼接起来。
 */
public class BucketSort {

    public  static  void main(String[] args) {
         int [] arr={ 8,7,10,12,1,2,6,6};
        bucketSort(arr);
    }

    public  static  void  bucketSort(int [] arr) {
        int max=Integer.MIN_VALUE;
        int min= Integer.MAX_VALUE;
        for(int an: arr) {
            max=Math.max(max, an);
            min=Math.min(min, an);
        }
        int bucketNum=(max-min)/ arr.length+1;
        ArrayList<ArrayList<Integer>> bucketArr=new ArrayList<>();
        for(int i=0;i<bucketNum;i++) {
            bucketArr.add(new ArrayList<>());
        }
        for(int an: arr) {
            int num=(an- min) /arr.length;
            bucketArr.get(num).add(an);
        }
        for(ArrayList<Integer> aBu :bucketArr ) {
            if(aBu.size()>0) {
                Collections.sort(aBu);
            }
        }

        for(ArrayList<Integer> aBu :bucketArr ) {
            if(aBu.size()>0) {
                System.out.println(aBu);
            }
        }
    }
}
