package org.fiuba.configuration;

import org.fiuba.domain.ItemBuilder;
import org.fiuba.domain.ItemBuilderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemBeanDefinition {

    @Bean
    public ItemBuilder itemBuilder() {
        return new ItemBuilderAdapter();
    }
}

