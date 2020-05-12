package org.springbus.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration

@PropertySources(value = {
        @PropertySource( value = "classpath:application.properties"),
        @PropertySource( value = "classpath:application-dev.properties")

})
public class PropConfigureTest {

}
