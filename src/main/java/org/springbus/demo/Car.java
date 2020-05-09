package org.springbus.demo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Car {

    private double price;

    private String brand;


}
