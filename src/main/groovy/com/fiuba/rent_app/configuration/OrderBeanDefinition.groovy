package com.fiuba.rent_app.configuration

import com.fiuba.rent_app.datasource.order.JpaOrderRepository
import com.fiuba.rent_app.domain.item.ItemRepository
import com.fiuba.rent_app.domain.order.OrderService
import com.fiuba.rent_app.domain.order.OrderServiceAdapter
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
    OrderService orderService(ItemRepository itemRepository, JpaOrderRepository jpaOrderRepository) {
        return new OrderServiceAdapter(itemRepository: itemRepository, orderRepository: jpaOrderRepository)
    }
}
