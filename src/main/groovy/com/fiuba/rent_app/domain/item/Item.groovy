package com.fiuba.rent_app.domain.item

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import java.time.Duration

@Entity(name = "item")
@Table(name = "item")
class Item {
    @Id
    private UUID id

    @Column(name = "renter")
    private Long renter

    @Column(name = "description")
    private String description

    @Column(name = "price")
    private BigDecimal price

    @Column(name = "rent_duration")
    private Duration rentDuration

    Item(UUID id, Long renter, String description, BigDecimal price, Duration rentDuration) {
        this.id = id
        this.renter = renter
        this.description = description
        this.price = price
        this.rentDuration = rentDuration
    }

    Item() {}

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
