package com.fiuba.rent_app.configuration


import com.fiuba.rent_app.datasource.item.JpaItemRepository

import com.fiuba.rent_app.domain.item.service.ItemService
import com.fiuba.rent_app.domain.item.service.ItemServiceAdapter
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponseFactory
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponseFactoryAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ItemBeanDefinition {

    @Bean
    ItemHttpResponseFactory itemHttpResponseFactory() {
        return new ItemHttpResponseFactoryAdapter()
    }

    @Bean
    ItemService itemService(JpaItemRepository repository) {
        return new ItemServiceAdapter(itemRepository: repository)
    }
}
