package com.fiuba.rent_app.configuration

import com.fiuba.rent_app.datasource.ItemRepositoryAdapter
import com.fiuba.rent_app.domain.item.ItemBuilder
import com.fiuba.rent_app.domain.item.ItemBuilderAdapter
import com.fiuba.rent_app.domain.item.ItemRepository
import com.fiuba.rent_app.domain.item.ItemService
import com.fiuba.rent_app.domain.item.ItemServiceAdapter
import com.fiuba.rent_app.presentation.ItemHttpResponseFactory
import com.fiuba.rent_app.presentation.ItemHttpResponseFactoryAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ItemBeanDefinition {

    @Bean
    ItemBuilder itemBuilder() {
        return new ItemBuilderAdapter()
    }

    @Bean
    ItemRepository itemRepository() {
        return new ItemRepositoryAdapter()
    }

    @Bean
    ItemHttpResponseFactory itemHttpResponseFactory() {
        return new ItemHttpResponseFactoryAdapter()
    }

    @Bean
    ItemService itemService(ItemRepository repository, ItemBuilder builder) {
        return new ItemServiceAdapter(repository, builder)
    }
}
