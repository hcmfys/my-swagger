package org.springbus.test;

import org.junit.Test;
import org.springframework.core.env.*;

import java.util.HashMap;
import java.util.Map;

public class Test2 {

    @Test
    public  void test2() {
        SystemEnvironmentPropertySource source =
                new SystemEnvironmentPropertySource("systemEnvironment", (Map) System.getenv());
        System.out.println(source.getProperty("PATH"));
        System.out.println(source.getProperty("PROCESSOR_LEVEL".toLowerCase()));
        System.out.println(source.getProperty("PROCESSOR.LEVEL"));

    }

    @Test
    public  void test3(){
        MutablePropertySources sources = new MutablePropertySources();
        sources.addLast(new MapPropertySource("map", new HashMap<String, Object>() {
            {
                put("name", "wang");
                put("age", 12);
            }
        }));//向MutablePropertySources添加一个MapPropertySource


        sources.addFirst(new MapPropertySource("map2", new HashMap<String, Object>() {
            {
                put("namex", "wang");
                put("age", 13);
            }
        }));//向MutablePropertySources添加一个MapPropertySource

        PropertyResolver resolver = new PropertySourcesPropertyResolver(sources);
        System.out.println(resolver.containsProperty("name"));//输出 true
        System.out.println(resolver.getProperty("age"));//输出 12
        System.out.println(resolver.resolvePlaceholders("My name is ${name} .I am ${age}."));


    }
}
