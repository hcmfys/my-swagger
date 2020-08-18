package org.springbus.leetcode;

//题目描述
//给你两个有序整数数组 nums1 和 nums2，请你将 nums2 合并到 nums1 中*，*使 nums1 成为一个有序数组。
//
//说明:
//
//初始化 nums1 和 nums2 的元素数量分别为 m 和 n 。
//你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素。
//示例:
//
//输入:
//nums1 = [1,2,3,0,0,0], m = 3
//nums2 = [2,5,6],       n = 3
//
//输出: [1,2,2,3,5,6]

public class _88合并两个有序数组 {



    public  static  void main(String[] args){
        int a[]={1,2,3,0,0,0};
        int b[]={2,5,6};
        merge(a,3,b, 3);
        for(int i=0;i<a.length;i++){
            System.out.print(a[i] +" ");
        }
    }

    public static  void merge(int[] number1, int m, int[] number2, int n){
        int size=m+n;
        int l=m-1;
        int j=n-1;
        int q=size;
        while(l>=0 && j>=0 ) {
            if( number1[l] > number2[j]) {
                number1[--q]=number1[l];
                l--;
            }else {
                number1[--q]=number2[j];
                j--;
            }
        }

        while (j>=0){
            number1[--q]=number2[j];
            j--;
        }
    }

}
