package org.springbus.anotion;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ResponseBody
@UrlRequestBody
@Inherited
@Documented
public @interface MyAnnation {
    String value() default "";
    String url() default "";
}
