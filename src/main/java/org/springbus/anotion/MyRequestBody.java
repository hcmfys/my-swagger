package org.springbus.anotion;


import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MyRequestBody {

}
