package org.springbus.comg;

import java.io.*;

public class ReadJis {

  static void toCode(String path) throws Exception {
    File root = new File(path);
    File[] fs = root.listFiles();
    for (File f : fs) {
      if (f.isDirectory()) {
        toCode(f.getAbsolutePath());
      } else {
        if (f.getName().endsWith(".class") || f.getName().endsWith(".jar") ||
          f.getName().endsWith(".html") || f.getName().endsWith(".txt")
        ) {
          f.delete();
        } else   {
          read(f.getAbsolutePath(), f.getAbsolutePath());
        }
      }
    }
    }


    public static void read(String path,String path1){
        File file = new File(path);
        File file1 = new File(path1);
        int rownum = 1;
        String row;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file),"Shift_JIS"));
            StringBuffer stringBuffer=new StringBuffer();
            while ((row = in.readLine()) != null) {
                rownum++;
                stringBuffer.append(row + "\r\n");
            }
            in.close();
            BufferedWriter out = new BufferedWriter(new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file1),
                            "UTF8")));
            out.write(stringBuffer.toString());
            out.close();

            System.out.println("convert " +path +"  ok ");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static  void main(String[] args) throws Exception {
            toCode("/Users/j2ee/Downloads/SJIS/");

    }
}
