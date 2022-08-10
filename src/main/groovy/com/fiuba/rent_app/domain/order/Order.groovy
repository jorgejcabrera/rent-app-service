package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.account.AccountWithDebtException
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.exception.InvalidCallerException
import com.fiuba.rent_app.domain.order.exception.OrderAlreadyFinishedException

import javax.persistence.*
import java.time.LocalDateTime

import static com.fiuba.rent_app.domain.order.Order.OrderStatus.*
import static java.time.LocalDateTime.now
import static javax.persistence.CascadeType.ALL
import static javax.persistence.EnumType.STRING
import static javax.persistence.GenerationType.AUTO

@Entity(name = "orders")
@Table(name = "orders")
class Order {

    enum OrderStatus {
        FINISHED,
        OPEN
    }

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    public
    Long id

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "order_id", nullable = false)
    Account account

    @Column(name = "created_at")
    private LocalDateTime createdAt

    @Column(name = "status")
    @Enumerated(STRING)
    private OrderStatus status

    @Column(name = "rent_day")
    private LocalDateTime rentDay

    Order(Item item, Account borrower) {
        this.account = borrower
        this.createdAt = now()
        this.item = item
        this.status = OPEN
        this.rentDay = now()
        if (!this.isValid()) {
            throw new InvalidCallerException("The borrower ${this.account.id} can't be the owner of the ${this.getItem().getId()} item")
        }
        if (borrower.hasDebt()) {
            throw new AccountWithDebtException("The borrower ${this.account.id} has items pending to be return")
        }
    }

    Long getLender() {
        return this.item.account.id
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

    Boolean isValid() {
        this.getLender() != this.account.id
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
