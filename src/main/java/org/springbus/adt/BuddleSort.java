package org.springbus.adt;

import java.util.ArrayList;
import java.util.Collections;

public class BuddleSort {

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
