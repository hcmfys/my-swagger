package org.springbus.asm;

import org.jooq.meta.derby.sys.Sys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Calc {

  private int add(int a, int b) {

    return (a + b);
  }

  private List<List<AsmTest>> asm;
  private Map<List<List<AsmTest>>, AsmTest> jsm;


    /**
     *
     * @param asm
     * @param jsm
     * @param itList
     */
  public  void  showMsg(List<List<AsmTest>> asm, Map<List<List<AsmTest>>, AsmTest> jsm, List<String> itList) {

  }



  public int cal() {
    int a = 100;
    int b = 200;
    int c = 300;
    return (a + b) * c;
  }

  private int ifCal() {
    int a = 100;
    int b = 200;
    int c = 300;
    if (a > b) {
      return a;
    }
    return b;
  }

 public static  String getSin(Type f){
      String  sign="L"+f.getTypeName().replace("<", "<L").replace(">", ";>").
              replace(".", "/").replace(", ", ";L")+";";
      return sign ;
  }

  public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
    Class calc = Calc.class;
    Field field = calc.getDeclaredField("asm");
    Type f = field.getGenericType();
    // System.out.println(f.getTypeName());
    String sign = getSin(f);
    System.out.println(sign);
    sign = getSin(calc.getDeclaredField("jsm").getGenericType());
    System.out.println(sign);
    Method method=  calc.getDeclaredMethod("showMsg", List.class, Map.class, List.class);
    Type[] types= method.getGenericParameterTypes();
    List<String> typeList=new ArrayList<>();
    for(Type fs : types) {
        typeList.add(getSin(fs));
    }
    System.out.println( String.join("", typeList));
  }
}
