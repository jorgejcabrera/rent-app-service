package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.datasource.order.JpaOrderRepository
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.ItemRepository

class OrderServiceAdapter implements OrderService {

    private ItemRepository itemRepository
    private JpaOrderRepository orderRepository

    @Override
    Order createFor(Long itemId, Long renterId) {
        Item item = getItemById(itemId)
        if (theRenterIsTheItemOwner(item.renter, renterId)) {
            throw new InvalidRenterException("The renter $renterId is the owner of the $itemId item")
        }
        item.rent()
        Order order = new OrderBuilderAdapter()
                .renter(renterId)
                .item(item)
                .build()
        return orderRepository.save(order)

    }

    private Item getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
        if (item.isBeingUsed()) {
            throw new ItemIsNotAvailableForOrderingException("The item $itemId is not avilable.")
        }
        return item
    }

    private static boolean theRenterIsTheItemOwner(long borrowerId, long renterId) {
        return borrowerId == renterId
    }
}

