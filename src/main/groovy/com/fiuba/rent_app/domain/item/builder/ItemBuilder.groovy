package com.fiuba.rent_app.domain.item.builder

import com.fiuba.rent_app.domain.item.Item


interface ItemBuilder {

    ItemBuilder id(Long id)

    ItemBuilder description(String description)

    ItemBuilder price(BigDecimal price)

    ItemBuilder renter(Long renter)

    ItemBuilder rentDaysDuration(int daysDuration)

    Item build()

}