package com.fiuba.rent_app.domain.order.builder.rule

import com.fiuba.rent_app.domain.order.Order

interface OrderCreationRule {
    void evaluate(Order order)
}