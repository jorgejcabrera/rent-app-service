package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.order.Order
import org.hibernate.annotations.Fetch
import org.springframework.data.repository.cdi.Eager

import javax.persistence.*
import java.time.Duration
import java.time.LocalDateTime

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

    @Column(name = "rent_day")
    private LocalDateTime rentDay

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

    Boolean canBeRented() {
        !this.isBeingUsed()
    }

    Boolean isBeingUsed() {
        this.orders.find { it.isOpen() } != null
    }

    LocalDateTime expireRentDay() {
        this.rentDay + this.rentDuration
    }

    void addOrder(Order order) {
        this.orders.add(order)
    }

    void republish(BigDecimal price, Duration rentDuration) {
        this.finishAllOrders()
        this.rentDuration = rentDuration
        this.price = price
    }

    private finishAllOrders() {
        this.orders.forEach { it.finish() }
    }

}
