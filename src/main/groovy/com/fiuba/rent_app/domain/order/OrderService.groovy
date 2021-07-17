package com.fiuba.rent_app.domain.order

interface OrderService {
    Order createFor(Long itemId, Long renterId)
}