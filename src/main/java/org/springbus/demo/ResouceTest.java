package org.springbus.demo;

import lombok.Synchronized;
import org.jooq.meta.derby.sys.Sys;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;

public class ResouceTest {


    private static void test1() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] rs = resolver.getResources("classpath:**/*.xml");
        for (Resource r : rs) {
            System.out.println("---->" + r);
        }
    }


    private static void test2() throws IOException {
         Object o=new Object();
         while(true) {
             System.out.println("=====");
             synchronized (o) {

                 o=null;
             }
         }
    }


    public static void main(String[] args) throws IOException {

        //test2();

    }
}
