package org.fiuba.domain;

import java.math.BigDecimal;
import java.util.UUID;

import static java.time.Duration.ofDays;

public class ItemBuilderAdapter implements ItemBuilder {

    private String description;
    private BigDecimal price;
    private Long renter;
    private Integer rentDaysDuration;

    @Override
    public ItemBuilder description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ItemBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    @Override
    public ItemBuilder renter(Long renter) {
        this.renter = renter;
        return this;
    }

    @Override
    public ItemBuilder rentDaysDuration(int daysDuration) {
        this.rentDaysDuration = daysDuration;
        return this;
    }

    @Override
    public Item build() {
        UUID itemId = UUID.randomUUID();
        return new Item(itemId,
                renter,
                description,
                price,
                ofDays(rentDaysDuration));
    }
}
