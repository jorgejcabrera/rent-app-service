package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.exception.InvalidCallerException
import com.fiuba.rent_app.domain.order.exception.OrderAlreadyFinishedException

import javax.persistence.*
import java.time.LocalDateTime

import static com.fiuba.rent_app.domain.order.OrderStatus.FINISHED
import static com.fiuba.rent_app.domain.order.OrderStatus.OPEN
import static java.time.LocalDateTime.now
import static javax.persistence.CascadeType.ALL
import static javax.persistence.EnumType.STRING
import static javax.persistence.GenerationType.AUTO

@Entity(name = "orders")
@Table(name = "orders")
class Order {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    public
    Long id

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

    @Column(name = "rent_day")
    private LocalDateTime rentDay

    Order(Item item, Account borrower) {
        this.lender = item.lenderId
        this.borrower = borrower.id
        this.createdAt = now()
        this.item = item
        this.status = OPEN
        this.rentDay = now()
        if (!this.isValid()) {
            throw new InvalidCallerException("The borrower ${this.getBorrower()} can't be the owner of the ${this.getItem().getId()} item")
        }
        if (borrower.hasDebt()) {
            throw new InvalidCallerException("The borrower ${this.getBorrower()} has items to be return pending")
        }
    }

    Order() {}

    LocalDateTime expireRentDay() {
        this.rentDay + this.item.rentDuration
    }

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

    Boolean canBeFinished() {
        this.status != FINISHED
    }

    void finish() {
        if (!canBeFinished()) {
            throw new OrderAlreadyFinishedException("The order ${this.id} is already closed")
        }
        this.status = FINISHED
    }

}
