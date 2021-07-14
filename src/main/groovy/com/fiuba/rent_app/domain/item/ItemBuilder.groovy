package com.fiuba.rent_app.domain.item


interface ItemBuilder {

    ItemBuilder description(String description);

    ItemBuilder price(BigDecimal price);

    ItemBuilder renter(Long renter);

    ItemBuilder rentDaysDuration(int daysDuration);

    Item build();

}