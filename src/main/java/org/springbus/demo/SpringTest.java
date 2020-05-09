package org.springbus.demo;

import org.jooq.meta.derby.sys.Sys;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class SpringTest {

    public  static  void main(String[] args) {


        Resource resource = new ClassPathResource("/car.xml");
        resource.getFilename();
        System.out.println(resource);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        Car bydCar = (Car) beanFactory.getBean("byd");
        System.out.println(bydCar);
        Person person = (Person) beanFactory.getBean("saber");
        System.out.println(person);
    }
}
