package com.fiuba.rent_app.domain.order.rule


import com.fiuba.rent_app.domain.order.Order

class ItemMustBeAvailableForOrdering implements OrderCreationRule {
    @Override
    void evaluate(Order order) {
        if (order.getItem().isBeingUsed()) {
            throw new ItemIsNotAvailableForOrderingException("The item ${order.getItem().getId()} is not avilable.")
        }
    }
}
