package org.springbus.test;

import org.jooq.meta.derby.sys.Sys;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

public class AppRun {

    public static void main(String[] args) {

        Reflections reflections = getFullReflections("org.springbus");
        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(RequestMapping.class);
        Iterator<Method> methodIterator = methodSet.iterator();
        while (methodIterator.hasNext()) {
            Method method = methodIterator.next();
            System.out.println(method);
        }
    }

    /**
     * 如果没有配置scanner，默认使用SubTypesScanner和TypeAnnotationsScanner
     *
     * @param basePackage 包路径
     */
    private static Reflections getFullReflections(String basePackage) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addUrls(ClasspathHelper.forPackage(basePackage));
        builder.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(),
                new MethodAnnotationsScanner(), new FieldAnnotationsScanner());
        builder.filterInputsBy(new FilterBuilder().includePackage(basePackage));

        Reflections reflections = new Reflections(builder);
        return reflections;
    }

    @Test
    public void testFastClass() throws Exception {
        A a = new A();
        a.setDa("duo");
        a.setShao(1);

        long start = System.nanoTime();
        FastClass fastA = FastClass.create(A.class);
        for (int i = 0; i < 5; i++) {
            fastA.invoke("setShao", new Class[]{Constants.TYPE_INTEGER.getClass()}, a, new Object[]{25} );
            Object o = fastA.invoke("getShao", Constants.EMPTY_CLASS_ARRAY, a, new Object[]{});
            System.out.println(o);
        }
        long end = System.nanoTime();
        System.out.println(end - start);
    }
}
