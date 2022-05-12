package com.fiuba.rent_app.domain.order.builder

import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.Order
import com.fiuba.rent_app.domain.order.OrderStatus
import com.fiuba.rent_app.domain.order.builder.exception.InvalidRenterException

import static com.fiuba.rent_app.domain.order.OrderStatus.*
import static java.time.LocalDateTime.now

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
        Order order = new Order(
                lender: item.lenderId,
                borrower: borrowerId,
                createdAt: now(),
                item: item,
                status: OPEN
        )
        if (!order.isValid()) {
            throw new InvalidRenterException("The borrower ${order.getBorrower()} can't be the owner of the ${order.getItem().getId()} item")
        }
        order
    }
}
