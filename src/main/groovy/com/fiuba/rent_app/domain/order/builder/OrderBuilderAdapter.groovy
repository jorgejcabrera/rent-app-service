package com.fiuba.rent_app.domain.order.builder

import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.Order

import java.time.LocalDateTime

class OrderBuilderAdapter implements OrderBuilder {

    private Item item

    private Long renterId

    @Override
    OrderBuilder item(Item item) {
        this.item = item
        return this
    }

    @Override
    OrderBuilder renterId(Long renterId) {
        this.renterId = renterId
        return this
    }

    @Override
    Order build() {
        LocalDateTime expiredRentDay = calculateExpiredRentDay(item)
        return new Order(
                renterId: renterId,
                borrower: item.borrowerId,
                expiredRentDay: expiredRentDay,
                item: item
        )
    }

    private static LocalDateTime calculateExpiredRentDay(Item item) {
        LocalDateTime expiredRentDay = LocalDateTime.now() + item.rentDuration
        return expiredRentDay
    }
}
