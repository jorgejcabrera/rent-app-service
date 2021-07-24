package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.domain.account.Account
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

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    Account account

    @Column(name = "description")
    private String description

    @Column(name = "price")
    private BigDecimal price

    @Column(name = "rent_duration")
    private Duration rentDuration

    @Enumerated(STRING)
    private ItemStatus status

    Item() {}

    Long getId() {
        return id
    }

    Long getBorrowerId() {
        return this.account.getId()
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

    void rentWith(Order order) {
        //this.order = order
        this.status = RENTED
    }

    Boolean isBeingUsed() {
        return this.status == RENTED
    }
}
