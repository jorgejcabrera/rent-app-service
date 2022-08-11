package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.account.AccountWithDebtException
import com.fiuba.rent_app.domain.item.exception.EmptyItemPriceException
import com.fiuba.rent_app.domain.item.exception.EmptyItemTitleException
import com.fiuba.rent_app.domain.item.exception.InvalidBorrowerIdException
import com.fiuba.rent_app.domain.item.exception.InvalidRentDurationException
import com.fiuba.rent_app.domain.item.exception.ItemInUseException
import com.fiuba.rent_app.domain.item.exception.ItemNotInUseException

import com.fiuba.rent_app.domain.order.Order

import javax.persistence.*
import java.time.Duration
import java.time.LocalDateTime

import static java.time.Duration.ofDays
import static javax.persistence.GenerationType.AUTO

@Entity(name = "item")
@Table(name = "item")
class Item {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    Account account

    @OneToMany(mappedBy = "item")
    private Set<Order> orders

    @Column(name = "description")
    private String description

    @Column(name = "title")
    private String title

    @Column(name = "price")
    private BigDecimal price

    @Column(name = "assurance_cost")
    private BigDecimal assuranceCost

    @Column(name = "rent_duration")
    private Duration rentDuration

    Item(Account lender, String description, BigDecimal price, String title, Integer rentDuration, BigDecimal assuranceCost) {
        this.account = lender
        this.description = description
        this.price = price
        this.title = title
        this.rentDuration = rentDuration != null ? ofDays(rentDuration) : null
        this.assuranceCost = assuranceCost
        this.validate()
    }

    Item() {
        this.orders = new HashSet<>()
    }

    Long getId() {
        id
    }

    Long getLenderId() {
        this.account.getId()
    }

    String getDescription() {
        description
    }

    String getTitle() {
        title
    }

    BigDecimal getPrice() {
        price
    }

    BigDecimal getTotalToPay() {
        this.price + this.assuranceCost
    }

    Duration getRentDuration() {
        rentDuration
    }

    private void validate() {
        if (this.price == null) {
            throw new EmptyItemPriceException("Price must be not empty")
        }
        if (this.title == null) {
            throw new EmptyItemTitleException("Title must be not empty")
        }
        if (this.rentDuration == null) {
            throw new InvalidRentDurationException("A rent duration must be specified")
        }
    }

    Boolean hasExpiredOrders() {
        this.orders.any { it.expireRentDay() < LocalDateTime.now() && it.isOpen() }
    }

    Boolean canBeRented() {
        !this.isBeingUsed()
    }

    Boolean isBeingUsed() {
        this.orders.any { it.isOpen() }
    }

    Boolean isBeingUsedBy(Long borrowerId) {
        this.orders.any { it.account.id == borrowerId && it.isOpen() }
    }

    void addOrder(Order order) {
        this.orders.add(order)
    }

    void update(BigDecimal price, Duration rentDuration) {
        if (this.account.hasDebt()) {
            throw new AccountWithDebtException("Before publishing it again, return all the items")
        }
        if (this.isBeingUsed()) {
            throw new ItemInUseException("The item is being used")
        }
        this.rentDuration = rentDuration
        this.price = price
    }

    void free(long borrowerId) {
        if (this.isBeingUsed() && !this.isBeingUsedBy(borrowerId)) {
            throw new InvalidBorrowerIdException("The item ${this.id} could not be returning by $borrowerId")
        }
        if (!this.isBeingUsed()) {
            throw new ItemNotInUseException("The item ${this.id} is not rented by anyone")
        }
        Order order = this.orders.find { it.isOpen() }
        order.finish()
    }

}
