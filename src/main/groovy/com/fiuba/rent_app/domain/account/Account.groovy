package com.fiuba.rent_app.domain.account

import com.fiuba.rent_app.domain.item.Item

import javax.persistence.*

import static javax.persistence.GenerationType.AUTO

@Entity(name = "account")
@Table(name = "account")
class Account {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id

    @Column(name = "email")
    private String email

    @OneToMany(mappedBy = "account")
    private Set<Item> items


    Long getId() {
        return this.id
    }
}
