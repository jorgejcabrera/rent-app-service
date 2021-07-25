package com.fiuba.rent_app.domain.order.builder.rule


import com.fiuba.rent_app.domain.order.Order

class ItemBorrowerAndOwnerMustBeDifferent implements OrderCreationRule {

    @Override
    void evaluate(Order order) {
        if (order.getLender() == order.getBorrower()) {
            throw new InvalidRenterException("The borrower ${order.getBorrower()} can't be the owner of the ${order.getItem().getId()} item")
        }
    }
}
