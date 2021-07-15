package com.fiuba.rent_app.domain.order

interface OrderService {
    Order createFor(String itemId, Long renterId)
}