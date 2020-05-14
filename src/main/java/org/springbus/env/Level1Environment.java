package org.springbus.env;


import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

@Component

public class Level1Environment extends AbstractEnvironment {
    @Override
    protected void customizePropertySources(MutablePropertySources propertySources) {
        super.customizePropertySources(propertySources); // no-op from base class


    }

}