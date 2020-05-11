package org.springbus.demo;

import lombok.Data;
import org.jooq.meta.derby.sys.Sys;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Data
//FactoryBean<Student>,
public class Student implements  BeanClassLoaderAware,
        ApplicationContextAware, BeanNameAware , InitializingBean, BeanFactoryAware  {

    private  ClassLoader classLoader;
    private  ApplicationContext applicationContext ;

    /**
    private  Student s;
    @Override
    public Student getObject() throws Exception {
        s= new Student();
        return  s;
    }
    /*

    @Override
    public Class<?> getObjectType() {
        return Student.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
    */

    public  void display(){
        System.out.println("name="+name);
        System.out.println("applicationContext="+applicationContext);
    }

    private int age;
    private String name;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader=classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println( " ===?>  setBeanName"    +name);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.age=12;
        this.name="my name";
        System.out.println( " ===?>-------->  afterPropertiesSet"   );
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println( " ===?>  beanFactory" +beanFactory);

    }


}
