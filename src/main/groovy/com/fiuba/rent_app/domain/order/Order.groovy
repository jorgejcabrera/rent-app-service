package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.domain.item.Item

import javax.persistence.*
import java.time.LocalDateTime

import static javax.persistence.CascadeType.ALL
import static javax.persistence.GenerationType.AUTO

// revisar cardinalidad many to onde desde order validando
@Entity(name = "orders")
@Table(name = "orders")
class Order {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item

    @Column(name = "lender_id")
    private Long lender

    @Column(name = "borrower_id")
    private Long borrower

    @Column(name = "expired_rent_day")
    private LocalDateTime expiredRentDay

    Order(Long lender, Long borrower, LocalDateTime expiredRentDay, Item item) {
        this.lender = lender
        this.borrower = borrower
        this.expiredRentDay = expiredRentDay
        this.item = item
    }

    Order() {}

    Item getItem() {
        this.item
    }

    Long getLender() {
        this.lender
    }

    Long getBorrower() {
        this.borrower
    }

    Boolean isValid() {
        this.getLender() != this.getBorrower()
    }
}
