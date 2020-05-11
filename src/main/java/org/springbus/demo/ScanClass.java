package org.springbus.demo;

public class ScanClass {

    /**
     *
     * @param args
     */
    public static  void main(String[] args) {
        CustomerClassScan customerClassScan=new CustomerClassScan();
        customerClassScan.loadClass("org");
    }
}
