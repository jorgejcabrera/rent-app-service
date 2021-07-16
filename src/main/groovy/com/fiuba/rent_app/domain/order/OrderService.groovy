package com.fiuba.rent_app.domain.order

interface OrderService {
    Order createFor(UUID itemId, Long renterId)
}