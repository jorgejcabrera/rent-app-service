package com.fiuba.rent_app.domain.order.builder

import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.Order
import com.fiuba.rent_app.domain.order.builder.rule.InvalidRenterException
import com.fiuba.rent_app.domain.order.builder.rule.ItemIsNotAvailableForOrderingException

import java.time.LocalDateTime

class OrderBuilderAdapter implements OrderBuilder {

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
        LocalDateTime expiredRentDay = calculateExpiredRentDay(item)
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

    private static LocalDateTime calculateExpiredRentDay(Item item) {
        LocalDateTime expiredRentDay = LocalDateTime.now() + item.rentDuration
        return expiredRentDay
    }
}
