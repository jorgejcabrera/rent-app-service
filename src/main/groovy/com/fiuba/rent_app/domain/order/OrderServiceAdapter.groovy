package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.ItemRepository

import java.time.LocalDateTime

class OrderServiceAdapter implements OrderService {

    private ItemRepository itemRepository
    private OrderRepository orderRepository

    OrderServiceAdapter(ItemRepository itemRepository, OrderRepository orderRepository) {
        this.itemRepository = itemRepository
        this.orderRepository = orderRepository
    }

    @Override
    Order createFor(UUID itemId, Long renterId) {
        Item item = getItemById(itemId)
        item.rent()
        Order order = create(item, renterId)
        orderRepository.save(order)
        /** Inline reference is not possible here, because the repository is a mock in
         * the unit test and I don't know the order creation details*/
        return order
    }

    private Item getItemById(UUID itemId) {
        Item item = itemRepository.findById(itemId)
        if (item.isBeingUsed()) {
            throw new ItemIsNotAvailableForOrderedException("The item $itemId is not avilable.")
        }
        return item
    }

    private Order create(Item item, long renterId) {
        LocalDateTime expiredRentDay = calculateExpiredRentDay(item)
        return new Order(renterId, item.renter, expiredRentDay, item)
    }

    private LocalDateTime calculateExpiredRentDay(Item item) {
        LocalDateTime expiredRentDay = LocalDateTime.now() + item.rentDuration
        return expiredRentDay
    }
}

