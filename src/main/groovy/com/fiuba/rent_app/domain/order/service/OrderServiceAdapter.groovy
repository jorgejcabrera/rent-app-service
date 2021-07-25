package com.fiuba.rent_app.domain.order.service

import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.datasource.order.JpaOrderRepository
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.Order
import com.fiuba.rent_app.domain.order.builder.OrderBuilderAdapter
import com.fiuba.rent_app.domain.order.builder.rule.OrderCreationRule

class OrderServiceAdapter implements OrderService {

    private JpaItemRepository itemRepository
    private JpaOrderRepository orderRepository
    private List<OrderCreationRule> orderCreationRules

    @Override
    Order createFor(Long itemId, Long borrowerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow { new ItemNotFoundException("Item $itemId does not exist") }
        Order order = new OrderBuilderAdapter()
                .borrowerId(borrowerId)
                .item(item)
                .build()
        orderCreationRules.forEach(rule -> rule.evaluate(order))
        item.rentWith()
        return orderRepository.save(order)
    }
}