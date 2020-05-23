package org.springbus;

import org.jooq.meta.derby.sys.Sys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App {

   public  static  void main(String[] args) {
       ConfigurableApplicationContext context= SpringApplication.run(App.class,args);

       String[] names=context.getBeanDefinitionNames();
       for(String name :names) {
           //System.out.println(" bean name-------------->" + name);
       }
    }
}
