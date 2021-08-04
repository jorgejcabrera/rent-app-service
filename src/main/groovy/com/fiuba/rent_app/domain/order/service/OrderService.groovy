package com.fiuba.rent_app.domain.order.service

import com.fiuba.rent_app.domain.order.Order

interface OrderService {
    Order createFor(Long itemId, Long borrowerId)
}