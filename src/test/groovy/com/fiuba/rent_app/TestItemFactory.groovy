package com.fiuba.rent_app

import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.builder.OrderBuilder
import com.fiuba.rent_app.domain.order.builder.OrderBuilderImpl

import java.time.Duration

import static java.math.BigDecimal.valueOf
import static java.time.Duration.ofDays
import static java.time.LocalDateTime.now

class TestItemFactory {

    static Item rentedDrillWith(Long lender) {
        def item = new Item(
                description: "Drill",
                rentDuration: ofDays(1),
                account: accountOf(lender))
        def order = new OrderBuilderImpl()
                .item(item)
                .borrowerId(2)
                .build()
        item.addOrder(order)
        item
    }

    static Item availableDrillWith(Long lender) {
        Duration rentDuration = ofDays(2)
        return new Item(
                rentDuration: rentDuration,
                rentDay: now(),
                price: valueOf(10L),
                description: "Drill",
                account: accountOf(lender))
    }

    private static Account accountOf(Long accountId) {
        return new Account(id: accountId, email: "jocabrera@fi.uba.com.ar")
    }
}
