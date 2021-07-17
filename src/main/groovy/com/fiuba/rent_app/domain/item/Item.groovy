package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.domain.order.Order

import javax.persistence.*
import java.time.Duration

import static com.fiuba.rent_app.domain.item.ItemStatus.RENTED
import static javax.persistence.CascadeType.ALL
import static javax.persistence.EnumType.STRING

@Entity(name = "item")
@Table(name = "item")
class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order

    @Column(name = "borrower")
    private Long borrower

    @Column(name = "description")
    private String description

    @Column(name = "price")
    private BigDecimal price

    @Column(name = "rent_duration")
    private Duration rentDuration

    @Enumerated(STRING)
    private ItemStatus status

    Item(Long borrower, String description, BigDecimal price, Duration rentDuration, ItemStatus status) {
        this.id = id
        this.borrower = borrower
        this.description = description
        this.price = price
        this.rentDuration = rentDuration
        this.status = status
    }

    Item() {}

    Long getId() {
        return id
    }

    Long getRenter() {
        return borrower
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
