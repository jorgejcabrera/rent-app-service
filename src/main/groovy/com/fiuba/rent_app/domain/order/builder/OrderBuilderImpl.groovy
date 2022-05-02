package com.fiuba.rent_app.domain.order.builder

import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.Order
import com.fiuba.rent_app.domain.order.builder.exception.InvalidRenterException

import java.time.LocalDateTime

class OrderBuilderImpl implements OrderBuilder {

    private Item item

    private Long borrowerId

    @Override
    OrderBuilder item(Item item) {
        this.item = item
        return this
    }

    @Override
    OrderBuilder borrowerId(Long borrowerId) {
        this.borrowerId = borrowerId
        return this
    }

    @Override
    Order build() {
        LocalDateTime expiredRentDay = item.calculateExpiredRentDay()
        Order order = new Order(
                lender: item.lenderId,
                borrower: borrowerId,
                expiredRentDay: expiredRentDay,
                item: item
        )
        if (!order.isValid()) {
            throw new InvalidRenterException("The borrower ${order.getBorrower()} can't be the owner of the ${order.getItem().getId()} item")
        }
        order
    }
}
