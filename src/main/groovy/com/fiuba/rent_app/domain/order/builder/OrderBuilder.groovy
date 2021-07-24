package com.fiuba.rent_app.domain.order.builder

import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.Order

interface OrderBuilder {

    OrderBuilder item(Item item)

    OrderBuilder renterId(Long renterId)

    Order build()
}