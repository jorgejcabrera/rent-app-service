package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.domain.order.Order

import javax.persistence.*
import java.time.Duration

import static com.fiuba.rent_app.domain.item.ItemStatus.AVAILABLE
import static com.fiuba.rent_app.domain.item.ItemStatus.RENTED

@Entity(name = "item")
@Table(name = "item")
class Item {
    @Id
    @Column(name = "id")
    private UUID id

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order

    @Column(name = "renter")
    private Long renter

    @Column(name = "description")
    private String description

    @Column(name = "price")
    private BigDecimal price

    @Column(name = "rent_duration")
    private Duration rentDuration

    @Enumerated(EnumType.STRING)
    private ItemStatus status

    Item(UUID id, Long renter, String description, BigDecimal price, Duration rentDuration) {
        this.id = id
        this.renter = renter
        this.description = description
        this.price = price
        this.rentDuration = rentDuration
        this.status = AVAILABLE
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

    void rent() {
        this.status = RENTED
    }

    Boolean isBeingUsed() {
        return this.status == RENTED
    }
}
