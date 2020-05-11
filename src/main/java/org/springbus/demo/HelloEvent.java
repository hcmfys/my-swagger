package org.springbus.demo;


import org.springframework.context.ApplicationEvent;


public class HelloEvent extends ApplicationEvent {

    private String name;

    public HelloEvent(Object source, String name) {
        super(source);
        this.name = name;
    }



    public HelloEvent(Object source) {
        super(source);
    }

    public String getName() {
        return name;
    }
}
