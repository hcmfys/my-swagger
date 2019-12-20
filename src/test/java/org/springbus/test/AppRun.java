package org.springbus.test;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

public class AppRun {

    public static void main(String[] args) {

        Reflections reflections=  getFullReflections ( "org.springbus");
        Set<Method>  methodSet=reflections.getMethodsAnnotatedWith(RequestMapping.class);
        Iterator<Method>  methodIterator= methodSet.iterator();
        while(methodIterator.hasNext()) {
            Method method = methodIterator.next();
            System.out.println(method);
        }
    }
    /**
     * 如果没有配置scanner，默认使用SubTypesScanner和TypeAnnotationsScanner
     * @param basePackage 包路径
     */
    private static Reflections getFullReflections(String basePackage){
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addUrls(ClasspathHelper.forPackage(basePackage));
        builder.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(),
                new MethodAnnotationsScanner(), new FieldAnnotationsScanner());
        builder.filterInputsBy(new FilterBuilder().includePackage(basePackage));

        Reflections reflections = new Reflections(builder);
        return reflections;
    }

}