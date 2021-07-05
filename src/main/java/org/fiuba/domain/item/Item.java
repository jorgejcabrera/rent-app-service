package org.fiuba.domain.item;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

public class Item {
    private UUID id;
    private Long renter;
    private String description;
    private BigDecimal price;
    private Duration rentDuration;

    public Item(UUID id, Long renter, String description, BigDecimal price, Duration rentDuration) {
        this.id = id;
        this.renter = renter;
        this.description = description;
        this.price = price;
        this.rentDuration = rentDuration;
    }

    public Item() {
    }

    public UUID getId() {
        return id;
    }

    public Long getRenter() {
        return renter;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Duration getRentDuration() {
        return rentDuration;
    }

}
