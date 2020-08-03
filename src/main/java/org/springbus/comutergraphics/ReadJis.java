package org.springbus.comutergraphics;

import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.Random;

public class ReadJis {

  static void toCode(String path) throws Exception {
    File root = new File(path);
    File[] fs = root.listFiles();
    for (File f : fs) {
      if (f.isDirectory()) {
        toCode(f.getAbsolutePath());
      } else {
        if (f.getName().endsWith(".class")
            || f.getName().endsWith(".jar")
            || f.getName().endsWith(".html")
            || f.getName().endsWith(".txt")) {
          f.delete();
        } else {
          read(f.getAbsolutePath(), f.getAbsolutePath());
        }
      }
    }
  }

  public static void read(String path, String path1) {
    File file = new File(path);
    File file1 = new File(path1);
    int rownum = 1;
    String row;
    try {
      BufferedReader in =
          new BufferedReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"));
      StringBuffer stringBuffer = new StringBuffer();
      while ((row = in.readLine()) != null) {
        rownum++;
        stringBuffer.append(row + "\r\n");
      }
      in.close();
      BufferedWriter out =
          new BufferedWriter(
              new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1), "UTF8")));
      out.write(stringBuffer.toString());
      out.close();

      System.out.println("convert " + path + "  ok ");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String tranv(String lan) throws IOException {
    int x=  new Random().nextInt(1000);
    String url =
        "http://translate.google.cn/translate_a/single?client=gtx&dt=t&dj=1&ie=UTF-8&sl=auto&tl=zh_cn&q="
            + lan;
    System.out.println("--->"+lan);
      try {
          Thread.sleep(1000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      Document doc = Jsoup.connect(url).ignoreContentType(true).get();
    if (doc != null) {
      String text = doc.text();
      text = JSON.parseObject(text).getJSONArray("sentences").getJSONObject(0).getString("trans");
      System.out.println(text);
      return text;
    }
      return "";
  }

  public static String readJavaFile(String path) {

    File file = new File(path);

    String row;
    try {
      BufferedReader in =
          new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
      StringBuffer stringBuffer = new StringBuffer();
      while ((row = in.readLine()) != null) {

        String line = row + "\r\n";
        int c = line.indexOf("//");
        if (c != -1) {
            String prexLine=line.substring(0,c);
            String nextLine=line.substring(c);
            String toLine=tranv(nextLine);
            if(toLine.equals("")){
                toLine=nextLine;
            }
            stringBuffer.append(prexLine+"//"+toLine);

        } else {

          stringBuffer.append(line);
        }
      }
      in.close();

      System.out.println(stringBuffer.toString());

    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static void main(String[] args) throws Exception {
    //   toCode("/Users/j2ee/Downloads/SJIS/");

    tranv("ベクトルの内積");
    readJavaFile(
        "/Volumes/f/java/app_project/my-swagger/src/main/java/org/springbus/comutergraphics/CG/common/Camera.java");
  }
}
