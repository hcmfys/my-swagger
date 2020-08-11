package org.springbus.collect;

import java.util.*;

public class CollecectList {


    static  void testList(){
        Collection<String> retList=new ArrayList<>();
        retList.add("One");
        retList.add("Two");
        retList.add("Three");
        System.out.println(retList);
    }
    static  void testSet(){
        Collection<String> retList=new HashSet<>();
        boolean a=retList.add("One");
        boolean b= retList.add("Two");
        boolean c= retList.add("Three");
        boolean d=retList.add("One");
        System.out.println(retList);
        System.out.println("a="+a  + " b="+b +" c="+c +" d ="+d );
    }

    static  void testTreeSet(){
        Collection<String> retList=new TreeSet<>();
        boolean a=retList.add("One");
        boolean b= retList.add("Two");
        boolean c= retList.add("Three");
        boolean d=retList.add("One");
        boolean e=retList.add("Ona");
        System.out.println(retList);
        System.out.println("a="+a  + " b="+b +" c="+c +" d ="+d  + " e:"+e);
    }

    static  void testTreeSetNum(){
        Collection<Integer> retList=new TreeSet<>();
        boolean a=retList.add(5);
        boolean b= retList.add(8);
        boolean c= retList.add(1);
        boolean d=retList.add(97);
        boolean e=retList.add(30);
        System.out.println(retList);
        System.out.println("a="+a  + " b="+b +" c="+c +" d ="+d  + " e:"+e);
    }


    static  void testTreeMap(){
        Map<Integer,String > retList=new TreeMap<>();
        String a=retList.put(51,"aa");
        String b= retList.put(18,"c");
        String c= retList.put(11,"c");
        String d=retList.put(197,null);
        String e=retList.put(130,"ksp");
        String f=retList.put(18,null);
        System.out.println(retList);
        retList.keySet().stream().forEach(item-> System.out.print(" " + item ) );
        System.out.println("a="+a  + " b="+b +" c="+c +" d ="+d  + " e:"+e +"  f="+f);
    }

    static  void testTreeHashMap(){
        Map<Integer,String > retList=new LinkedHashMap<>();
        String a=retList.put(5,"aa");
        String b= retList.put(8,"c");
        String c= retList.put(1,"c");
        String d=retList.put(97,null);
        String e=retList.put(30,"ksp");
        String f=retList.put(8,null);
        System.out.println(retList);
        retList.keySet().stream().forEach( item-> System.out.print(" " + item ) );
        System.out.println("a="+a  + " b="+b +" c="+c +" d ="+d  + " e:"+e +"  f="+f);
    }



    public  static  void  main(String[] args) {
        //testTreeSet();
       // testTreeSetNum();
        //testTreeMap();
        testTreeMap();
        testTreeHashMap();
    }
}
