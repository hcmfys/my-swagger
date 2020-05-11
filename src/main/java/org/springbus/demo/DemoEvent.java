package org.springbus.demo;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DemoEvent  implements ApplicationListener<HelloEvent> {


    @Override
    public void onApplicationEvent(HelloEvent event) {
        System.out.println("--------->" + event.toString());
    }
}
