package com.fiuba.rent_app.domain.item


import java.time.Duration

class Item {
    private UUID id
    private Long renter
    private String description
    private BigDecimal price
    private Duration rentDuration

    Item(UUID id, Long renter, String description, BigDecimal price, Duration rentDuration) {
        this.id = id
        this.renter = renter
        this.description = description
        this.price = price
        this.rentDuration = rentDuration
    }

    Item() {
    }

    UUID getId() {
        return id
    }

    Long getRenter() {
        return renter
    }

    String getDescription() {
        return description
    }

    BigDecimal getPrice() {
        return price
    }

    Duration getRentDuration() {
        return rentDuration
    }
}
