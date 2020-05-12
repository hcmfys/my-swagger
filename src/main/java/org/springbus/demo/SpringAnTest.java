package org.springbus.demo;

import com.sun.org.apache.bcel.internal.generic.StackConsumer;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class SpringAnTest {

    private   static  void test1(){

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        AnnotatedBeanDefinitionReader anReader=new AnnotatedBeanDefinitionReader(beanFactory);

        anReader.registerBean(Car.class, "byd");
        anReader.registerBean(Person.class, "saber");


        Car bydCar = (Car) beanFactory.getBean("byd");
        System.out.println(bydCar);
        Person person = (Person) beanFactory.getBean("saber");

        System.out.println(person);
    }



    private   static  void test2() {
        AnnotationConfigApplicationContext applicationContext = new
                AnnotationConfigApplicationContext();
        applicationContext.setEnvironment(new StandardEnvironment());
        applicationContext.scan("org.springbus.demo");
        applicationContext.refresh();

        Car bydCar = applicationContext.getBean(Car.class);
        applicationContext.publishEvent(new HelloEvent(bydCar, "name"));
        System.out.println(bydCar);
        Person person = applicationContext.getBean(Person.class);
        Student student = (Student) applicationContext.getBean("student");

        System.out.println(person);
        System.out.println(student);
        String h = applicationContext.getEnvironment().getProperty("d");
        student.display();
        System.out.println("h=" + h);

        String g = applicationContext.getEnvironment().getProperty("g");

        System.out.println("g=" + g);

        String c = applicationContext.getEnvironment().getProperty("c");

        System.out.println("c=" +c);

    }



    public  static  void main(String[] args) {

        test2();
    }
}
