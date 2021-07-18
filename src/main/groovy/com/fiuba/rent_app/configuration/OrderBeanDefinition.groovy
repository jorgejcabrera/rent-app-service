package com.fiuba.rent_app.configuration

import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.datasource.order.JpaOrderRepository

import com.fiuba.rent_app.domain.order.builder.rule.ItemMustBeAvailableForOrdering
import com.fiuba.rent_app.domain.order.builder.rule.ItemRenterAndOwnerMustBeDifferent
import com.fiuba.rent_app.domain.order.service.OrderService
import com.fiuba.rent_app.domain.order.service.OrderServiceAdapter
import com.fiuba.rent_app.presentation.order.response.OrderHttpResponseFactory
import com.fiuba.rent_app.presentation.order.response.OrderHttpResponseFactoryAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OrderBeanDefinition {

    @Bean
    OrderHttpResponseFactory orderHttpResponseFactory() {
        return new OrderHttpResponseFactoryAdapter()
    }

    @Bean
    OrderService orderService(JpaItemRepository itemRepository, JpaOrderRepository jpaOrderRepository) {
        return new OrderServiceAdapter(
                itemRepository: itemRepository,
                orderRepository: jpaOrderRepository,
                orderCreationRules: [new ItemMustBeAvailableForOrdering(), new ItemRenterAndOwnerMustBeDifferent()]
        )
    }
}
