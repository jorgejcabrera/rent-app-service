package com.fiuba.rent_app.domain.account

import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.Order

import javax.persistence.*
import java.time.LocalDateTime

import static javax.persistence.GenerationType.AUTO

@Entity(name = "account")
@Table(name = "account")
class Account {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    Long id

    @Column(name = "email")
    private String email

    @OneToMany(mappedBy = "account")
    private Set<Item> items

    @OneToMany(mappedBy = "account")
    private Set<Order> orders = new HashSet<Order>()

    Boolean hasDebt() {
        return this.orders.any{ it.expireRentDay() < LocalDateTime.now() && it.isOpen()}
    }

    Long getId() {
        return this.id
    }

    void addOrder(Order order) {
        this.orders.add(order)
    }
}
