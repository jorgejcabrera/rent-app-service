package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.domain.item.Item

import java.time.LocalDateTime

class OrderBuilderAdapter implements OrderBuilder {

    private Item item

    private Long renter

    @Override
    OrderBuilder item(Item item) {
        this.item = item
        return this
    }

    @Override
    OrderBuilder renter(Long renterId) {
        this.renter = renterId
        return this
    }

    @Override
    Order build() {
        LocalDateTime expiredRentDay = calculateExpiredRentDay(item)
        return new Order(renter, item.borrower, expiredRentDay, item)
    }

    private static LocalDateTime calculateExpiredRentDay(Item item) {
        LocalDateTime expiredRentDay = LocalDateTime.now() + item.rentDuration
        return expiredRentDay
    }
}
