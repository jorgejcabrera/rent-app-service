package com.fiuba.rent_app.domain.item


interface ItemBuilder {

    ItemBuilder id(Long id)

    ItemBuilder description(String description)

    ItemBuilder price(BigDecimal price)

    ItemBuilder renter(Long renter)

    ItemBuilder rentDaysDuration(int daysDuration)

    Item build()

}