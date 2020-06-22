package org.springbus.right;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class RightGrant {


    static  void init(){
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            String contextual2DNavigation = System.getProperty("com.sun.javafx.virtualKeyboard");
            System.out.println(contextual2DNavigation);
            return null;
        });
    }

    static  void grantIt(){

        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            boolean contextual2DNavigation = Boolean.getBoolean(
                    "com.sun.javafx.isContextual2DNavigation");
            String s = System.getProperty("com.sun.javafx.twoLevelFocus");
            if (s != null) {
                Boolean hasTwoLevelFocus = Boolean.valueOf(s);
            }
            s = System.getProperty("com.sun.javafx.virtualKeyboard");
            if (s != null) {
                boolean hasVirtualKeyboard;
                if (s.equalsIgnoreCase("none")) {
                    hasVirtualKeyboard = false;
                } else if (s.equalsIgnoreCase("javafx")) {
                    hasVirtualKeyboard = true;
                } else if (s.equalsIgnoreCase("native")) {
                    hasVirtualKeyboard = true;
                }
            }
            s = System.getProperty("com.sun.javafx.touch");
            if (s != null) {
              boolean  hasTouch = Boolean.valueOf(s);
            }
            s = System.getProperty("com.sun.javafx.multiTouch");
            if (s != null) {
                boolean  hasMultiTouch = Boolean.valueOf(s);
            }
            s = System.getProperty("com.sun.javafx.pointer");
            if (s != null) {
                boolean  hasPointer = Boolean.valueOf(s);
            }
            s = System.getProperty("javafx.embed.singleThread");
            if (s != null) {
                boolean  isThreadMerged = Boolean.valueOf(s);
            }
            return null;
        });

    }

    public  static  void  main(String[] args) {
        init();
    }
}
