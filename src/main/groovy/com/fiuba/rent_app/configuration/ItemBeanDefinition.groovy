package com.fiuba.rent_app.configuration

import com.fiuba.rent_app.datasource.item.ItemRepositoryAdapter
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.item.ItemRepository
import com.fiuba.rent_app.domain.item.ItemService
import com.fiuba.rent_app.domain.item.ItemServiceAdapter
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponseFactory
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponseFactoryAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ItemBeanDefinition {

    @Bean
    ItemRepository itemRepository(JpaItemRepository jpaItemRepository) {
        return new ItemRepositoryAdapter(jpaItemRepository)
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