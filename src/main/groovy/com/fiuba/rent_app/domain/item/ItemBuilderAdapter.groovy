package com.fiuba.rent_app.domain.item

import static java.time.Duration.ofDays

class ItemBuilderAdapter implements ItemBuilder {
    private String description
    private BigDecimal price
    private Long renter
    private Integer rentDaysDuration

    @Override
    ItemBuilder description(String description) {
        this.description = description
        return this
    }

    @Override
    ItemBuilder price(BigDecimal price) {
        this.price = price
        return this
    }

    @Override
    ItemBuilder renter(Long renter) {
        this.renter = renter
        return this
    }

    @Override
    ItemBuilder rentDaysDuration(int daysDuration) {
        this.rentDaysDuration = daysDuration
        return this
    }

    @Override
    Item build() {
        UUID itemId = UUID.randomUUID()
        return new Item(itemId,
                renter,
                description,
                price,
                ofDays(rentDaysDuration))
    }
}
