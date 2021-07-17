package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.domain.item.Item

interface OrderBuilder {

    OrderBuilder item(Item item)

    OrderBuilder renter(Long renterId)

    Order build()
}