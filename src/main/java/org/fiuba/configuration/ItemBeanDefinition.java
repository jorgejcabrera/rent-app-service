package org.fiuba.configuration;

import org.fiuba.datasource.ItemRepositoryAdapter;
import org.fiuba.domain.item.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemBeanDefinition {

    @Bean
    public ItemBuilder itemBuilder() {
        return new ItemBuilderAdapter();
    }

    @Bean
    public ItemRepository itemRepository() {
        return new ItemRepositoryAdapter();
    }

    @Bean
    public ItemService itemService(ItemRepository repository, ItemBuilder builder) {
        return new ItemServiceAdapter(repository, builder);
    }
}

