package com.fiuba.rent_app.domain.item

import static com.fiuba.rent_app.domain.item.ItemStatus.*
import static java.time.Duration.ofDays

class ItemBuilderAdapter implements ItemBuilder {
    private String description
    private BigDecimal price
    private Long renter
    private Long id
    private Integer rentDaysDuration

    @Override
    ItemBuilder id(Long id) {
        this.id = id
        return this
    }

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
        return new Item(
                borrower: renter,
                description: description,
                price: price,
                rentDuration: ofDays(rentDaysDuration),
                status: AVAILABLE,
                id: id)
    }
}
