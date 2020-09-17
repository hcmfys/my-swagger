package org.springbus.adt;


import com.google.common.collect.Lists;
import org.jooq.meta.derby.sys.Sys;

import java.awt.peer.LabelPeer;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DG {


      public void care(){
        //doCare(5);
          doCareLetter("([{",new ArrayList<String>());
      }

      public void  doCare(int a){
          if(a==0) {
              return;
          }
          doCare(a-1);
          System.out.print(a +" ");
      }

    public void  doCareLetter(String a, List<String> lst) {
        if (lst.size() == a.length()) {
            System.out.println(lst.stream().collect(Collectors.joining("")));
            return;
        }
        for (int j = 0; j < a.length(); j++) {
            String s = String.valueOf(a.charAt(j) );
            if (lst.contains(s)) {
                continue;
            }
            lst.add(s);
            doCareLetter(a, lst);
            lst.remove(s);
        }
    }

    Map<Integer,Long > met=new HashMap();
    long fib(int a) {


        if (a == 1 || a == 2) {
            met.put(a, 1L);
            return 1;
        }
        if (met.get(a) != null && met.get(a) > 0) {
            return met.get(a);
        }
        long c = fib(a - 1) + fib(a - 2);
        met.put(a, c);
        return c;
    }


    long fib_dp(int n){
        long[] dp=new long[n+1];
        dp[1]=dp[2]=1;
        dp[0]=0;
        for( int i=3;i<=n;i++) {
            dp[i]=dp[i-1] +dp[i-2];
        }
        return dp[n];
    }

    long fib_final(int n){
        if(n<=2) {
            return  1;
        }
        long sum=0;
        long prev=1;
        long next=1;
        for(int i=3;i<=n;i++) {
            sum=prev+next;
            prev=next;
            next=sum;
        }
        return  sum;
    }


    int dp_coin(List<Integer> coins,int amount) {
        int res=amount+1;
        if  (amount  == 0 )  return 0;
        if  (amount< 0) return -1;

        System.out.print (" amount  "+ amount );
        for( Integer coin : coins){
            System.out.println  ("  coin   "+ coin );
            res=Math.min(res,  1+dp_coin(coins,  amount- coin));
        }
        return  res;

    }

    void dp_cal(List<Integer> calList, int index, int amount, LinkedList<Integer> numList){

        if(numList.size()==calList.size()) {
            int res=0;
            for(Integer c: numList) {
                res+=c;
            }
            if(res == amount) {
                System.out.println(numList);
            }
            return ;
        }

        int d=calList.get(index);
        int dl[]={d,-d};
        for( int i=0;i<dl.length;i++) {
            int c = dl[i];
            numList.add( c);
            dp_cal(calList, index + 1, amount, numList);
            numList.removeLast();
        }

    }




    public static void main(String[] args) {
        // new DG().care();
        // System.out.println(new DG().fib(100));
        //System.out.println(new DG().fib_dp(100));
        //System.out.println(new DG().fib_final(100));
        //System.out.println(new DG().dp_coin(Lists.newArrayList(1,2,5),24));
        new DG().dp_cal(Lists.newArrayList(1, 1, 1, 1, 1), 0, 3, new LinkedList<>());
    }
}
