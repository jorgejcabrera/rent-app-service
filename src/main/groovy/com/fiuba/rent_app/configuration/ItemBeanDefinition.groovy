package com.fiuba.rent_app.configuration

import com.fiuba.rent_app.datasource.ItemRepositoryAdapter
import com.fiuba.rent_app.domain.item.ItemBuilder
import com.fiuba.rent_app.domain.item.ItemBuilderAdapter
import com.fiuba.rent_app.domain.item.ItemRepository
import com.fiuba.rent_app.domain.item.ItemService
import com.fiuba.rent_app.domain.item.ItemServiceAdapter
import com.fiuba.rent_app.presentation.item.ItemHttpResponseFactory
import com.fiuba.rent_app.presentation.item.ItemHttpResponseFactoryAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ItemBeanDefinition {

    @Bean
    ItemRepository itemRepository() {
        return new ItemRepositoryAdapter()
    }

    @Bean
    ItemHttpResponseFactory itemHttpResponseFactory() {
        return new ItemHttpResponseFactoryAdapter()
    }

    @Bean
    ItemService itemService(ItemRepository repository) {
        return new ItemServiceAdapter(repository)
    }
}
