package com.fiuba.rent_app.configuration

import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.item.service.ItemService
import com.fiuba.rent_app.domain.item.service.ItemServiceImpl
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
    ItemService itemService(
            JpaItemRepository itemRepository,
            JpaAccountRepository accountRepository
    ) {
        return new ItemServiceImpl(
                itemRepository: itemRepository,
                accountRepository: accountRepository
        )
    }
}
