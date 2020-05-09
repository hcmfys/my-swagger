package org.springbus.demo;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class Person {

    private String name;

    private Car car;

}
