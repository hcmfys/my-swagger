package org.springbus.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jooq.meta.derby.sys.Sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Data

@NoArgsConstructor
public class Car {
 
    private String name;
    private int id;
    private String color;
 
    public Car(String name, int id, String color) {
        this.name = name;
        this.id = id;
        this.color = color;
    }
    //constructors, getter/setters
    public static void main(String[] args){
        List<Car> cars = Arrays.asList(
                new Car("golf", 1, "red"),
                new Car("bmw", 2, "red"),
                new Car("mini", 3, "black"),
                new Car("ds", 4, "white"),
                new Car("bmw", 5, "gold"),
                new Car("golf", 6, "red"),
                new Car("bmw", 7, "gold"),
                new Car("ds", 8,"white")
        );
        Map<String, List<Car>> collect = cars.stream().collect(Collectors.groupingBy(
                Car::getName));
        System.out.println(collect);

        List<Car> carsList=new ArrayList<>();
        cars.stream().collect(Collectors.groupingBy(
                Car::getName)).forEach(new BiConsumer<String, List<Car>>() {
            @Override
            public void accept(String s, List<Car> cars) {
                System.out.println("s==>" +s +"  --> " +cars);
                Car car=new Car();
                car.setName(s);
                int ids=cars.stream().collect(Collectors.counting()).intValue();
                car.setId(ids);
               car.setColor( cars.stream().map(Car::getName).collect(Collectors.toList()).
                        stream().collect(Collectors.joining("-")));
                carsList.add(car);
            }
        });

        System.out.println(carsList);


    }

}
