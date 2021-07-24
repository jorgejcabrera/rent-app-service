package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.domain.item.Item

import javax.persistence.*
import java.time.LocalDateTime

import static javax.persistence.CascadeType.ALL
import static javax.persistence.GenerationType.AUTO

@Entity(name = "orders")
@Table(name = "orders")
class Order {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id

    @OneToOne(mappedBy = "order", cascade = ALL)
    private Item item

    @Column(name = "renter_id")
    private Long renterId

    @Column(name = "borrower_id")
    private Long borrower

    @Column(name = "expired_rent_day")
    private LocalDateTime expiredRentDay

    Order(Long renter, Long borrower, LocalDateTime expiredRentDay, Item item) {
        this.renterId = renter
        this.borrower = borrower
        this.expiredRentDay = expiredRentDay
        this.item = item
    }

    Order() {}

    Item getItem() {
        return this.item
    }

    Long getRenter() {
        return this.renterId
    }
}
