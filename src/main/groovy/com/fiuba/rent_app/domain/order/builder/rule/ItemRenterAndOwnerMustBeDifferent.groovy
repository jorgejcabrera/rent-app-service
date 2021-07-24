package com.fiuba.rent_app.domain.order.builder.rule


import com.fiuba.rent_app.domain.order.Order

class ItemRenterAndOwnerMustBeDifferent implements OrderCreationRule {

    @Override
    void evaluate(Order order) {
        if (order.getItem().getBorrowerId() == order.getRenter()) {
            throw new InvalidRenterException("The renter ${order.getRenter()} can't be the owner of the ${order.getItem().getId()} item")
        }
    }
}
