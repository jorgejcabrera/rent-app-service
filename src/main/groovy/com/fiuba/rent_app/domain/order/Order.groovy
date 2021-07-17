package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.domain.item.Item

import javax.persistence.*
import java.time.LocalDateTime

@Entity(name = "orders")
@Table(name = "orders")
class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id

    @OneToOne(mappedBy = "order")
    private Item item

    @Column(name = "renter_id")
    private Long renter

    @Column(name = "borrower_id")
    private Long borrower

    @Column(name = "expired_rent_day")
    private LocalDateTime expiredRentDay

    Order(Long renter, Long borrower, LocalDateTime expiredRentDay, Item item) {
        this.renter = renter
        this.borrower = borrower
        this.expiredRentDay = expiredRentDay
        this.item = item
    }

    Order() {}

    Item getItem() {
        return this.item
    }

    Long getRenter() {
        return this.renter
    }
}
