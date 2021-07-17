package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.datasource.order.JpaOrderRepository
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.ItemRepository
import com.fiuba.rent_app.domain.order.rule.OrderCreationRule

class OrderServiceAdapter implements OrderService {

    private ItemRepository itemRepository
    private JpaOrderRepository orderRepository
    private List<OrderCreationRule> orderCreationRules

    @Override
    Order createFor(Long itemId, Long renterId) {
        Item item = itemRepository.findById(itemId)
        Order order = new OrderBuilderAdapter()
                .renter(renterId)
                .item(item)
                .build()
        orderCreationRules.forEach(rule -> rule.evaluate(order))
        item.rent()
        return orderRepository.save(order)
    }
}