package org.springbus.valid;

import lombok.Data;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;


@Data
public class Student {


    @NotEmpty(message = "不能为空")
    private  String userName;
    @Range( min = 1 ,max = 30,message = "最大值不能小于0")
    private int age;

}
