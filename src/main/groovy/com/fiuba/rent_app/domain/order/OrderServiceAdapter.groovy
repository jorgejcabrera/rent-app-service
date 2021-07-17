package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.datasource.order.JpaOrderRepository
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.ItemRepository

import java.time.LocalDateTime

class OrderServiceAdapter implements OrderService {

    private ItemRepository itemRepository
    private JpaOrderRepository orderJpaRepository

    OrderServiceAdapter(
            ItemRepository itemRepository,
            JpaOrderRepository orderJpaRepository
    ) {
        this.itemRepository = itemRepository
        this.orderJpaRepository = orderJpaRepository
    }

    @Override
    Order createFor(Long itemId, Long renterId) {
        Item item = getItemById(itemId)
        if (theRenterIsTheItemOwner(item.renter, renterId)) {
            throw new InvalidRenterException("The renter $renterId is the owner of the $itemId item")
        }
        item.rent()
        Order order = create(item, renterId)
        orderJpaRepository.save(order)
        /** Inline reference is not possible here, because the repository is a mock in
         * the unit test and I don't know the order creation details*/
        return order
    }

    private Item getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
        if (item.isBeingUsed()) {
            throw new ItemIsNotAvailableForOrderingException("The item $itemId is not avilable.")
        }
        return item
    }

    private static Order create(Item item, long renterId) {
        LocalDateTime expiredRentDay = calculateExpiredRentDay(item)
        return new Order(renterId, item.renter, expiredRentDay, item)
    }

    private static LocalDateTime calculateExpiredRentDay(Item item) {
        LocalDateTime expiredRentDay = LocalDateTime.now() + item.rentDuration
        return expiredRentDay
    }

    private static boolean theRenterIsTheItemOwner(long borrowerId, long renterId) {
        return borrowerId == renterId
    }
}

