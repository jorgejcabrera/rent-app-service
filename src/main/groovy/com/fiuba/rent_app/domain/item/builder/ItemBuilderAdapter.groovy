package com.fiuba.rent_app.domain.item.builder

import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item

import static com.fiuba.rent_app.domain.item.ItemStatus.AVAILABLE
import static java.time.Duration.ofDays

class ItemBuilderAdapter implements ItemBuilder {
    private String description
    private BigDecimal price
    private BigDecimal assuranceCost
    private Account lender
    private String title
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
    ItemBuilder title(String title) {
        this.title = title
        return this
    }

    @Override
    ItemBuilder price(BigDecimal price) {
        this.price = price
        return this
    }

    @Override
    ItemBuilder lender(Account lender) {
        this.lender = lender
        return this
    }

    @Override
    ItemBuilder rentDaysDuration(int daysDuration) {
        this.rentDaysDuration = daysDuration
        return this
    }

    @Override
    ItemBuilder assuranceCost(BigDecimal assuranceCost) {
        this.assuranceCost = assuranceCost
        return this
    }

    @Override
    Item build() {
        return new Item(
                account: lender,
                description: description,
                price: price,
                title: title,
                rentDuration: ofDays(rentDaysDuration),
                status: AVAILABLE,
                assuranceCost: assuranceCost,
                id: id)
    }
}
