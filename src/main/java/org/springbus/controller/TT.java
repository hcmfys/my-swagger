package org.springbus.controller;

import org.jooq.meta.derby.sys.Sys;

public class TT {

  public static void main(String[] args) {

    // [1,2,87,87,87,2,1]
    // 1, 2, 2,1
    int t[] = {1, 2, 87, 87, 87, 2, 1};
    // t = new int[] {1, 0, 2};
     t = new int[] {1, 2, 3, 1, 0};
     //t=new int[]{1,3,4,5,2};
    candy(t);
  }

  public static int candy(int[] ratings) {

    if (ratings == null) {
      return 0;
    }
    if (ratings.length == 1) {
      return 1;
    }

    int tt[] = new int[ratings.length];
    tt[0] = 1;
    for (int i = 1; i < ratings.length ; i++) {
      tt[i] = 1;
      int a = ratings[i - 1];
      int b = ratings[i];
        if (b > a) {
        tt[i] = tt[i-1] + 1;
      }
    }

    for (int i = ratings.length-1; i >= 1; i--) {
      int a = ratings[i];
      int b = ratings[i - 1];
      if (a < b && tt[i-1] <=tt[i] ) {
        tt[i-1]=tt[i]+1;
      }
    }



    int rets = 0;
    for (int i = 0; i < tt.length; i++) {
      System.out.println("[" + i + "]=" + ratings[i] + "  -->" + tt[i]);
      rets += tt[i];
    }
    System.out.println(rets);
    return rets;
  }

}
