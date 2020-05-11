package org.springbus.demo;

import org.jooq.meta.derby.sys.Sys;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

@Component
public class AppWre   implements BeanNameAware, BeanFactoryAware {
    @Override
    public void setBeanName(String name) {
        System.out.println("set name="+name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("set beanFactory="+beanFactory);
    }
}
