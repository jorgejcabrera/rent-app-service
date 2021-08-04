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

    @OneToOne(mappedBy = "item", cascade = ALL)
    private Order order

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    Account account

    @Column(name = "description")
    private String description

    @Column(name = "title")
    private String title

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

    Long getLenderId() {
        return this.account.getId()
    }

    String getDescription() {
        return description
    }

    String getTitle() {
        return title
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
