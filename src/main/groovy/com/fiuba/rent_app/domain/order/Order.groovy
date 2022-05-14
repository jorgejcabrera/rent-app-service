package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.domain.item.Item

import javax.persistence.*
import java.time.LocalDateTime

import static com.fiuba.rent_app.domain.order.OrderStatus.FINISHED
import static com.fiuba.rent_app.domain.order.OrderStatus.OPEN
import static javax.persistence.CascadeType.ALL
import static javax.persistence.EnumType.STRING
import static javax.persistence.GenerationType.AUTO

@Entity(name = "orders")
@Table(name = "orders")
class Order {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item

    @Column(name = "lender_id")
    private Long lender

    @Column(name = "borrower_id")
    private Long borrower

    @Column(name = "created_at")
    private LocalDateTime createdAt

    @Column(name = "status")
    @Enumerated(STRING)
    private OrderStatus status

    Order(Long lender, Long borrower, LocalDateTime createdAt, Item item) {
        this.lender = lender
        this.borrower = borrower
        this.createdAt = createdAt
        this.item = item
        this.status = OPEN
    }

    Order() {}

    LocalDateTime createdAt() {
        return this.createdAt()
    }

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

    Boolean isOpen() {
        this.status == OPEN
    }

    void finish() {
        this.status = FINISHED
    }
}
