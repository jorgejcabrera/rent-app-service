package com.fiuba.rent_app.domain.order.service

import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.datasource.order.JpaOrderRepository
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.Order
import com.fiuba.rent_app.domain.order.builder.OrderBuilderImpl
import com.fiuba.rent_app.domain.order.builder.exception.ItemIsNotAvailableForOrderingException
import org.springframework.transaction.annotation.Transactional

class OrderServiceAdapter implements OrderService {

    private JpaItemRepository itemRepository
    private JpaOrderRepository orderRepository

    @Override
    @Transactional
    Order createFor(Long itemId, Long borrowerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow { new ItemNotFoundException("Item $itemId does not exist") }
        if (!item.canBeRented()) {
            throw new ItemIsNotAvailableForOrderingException("The item ${item.getId()} is not avilable.")
        }
        Order order = new OrderBuilderImpl()
                .borrowerId(borrowerId)
                .item(item)
                .build()
        orderRepository.save(order)
    }

}