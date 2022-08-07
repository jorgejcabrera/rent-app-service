package com.fiuba.rent_app

import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.Order
import com.fiuba.rent_app.domain.order.OrderStatus

import java.time.Duration

import static java.math.BigDecimal.valueOf
import static java.time.Duration.ofDays
import static java.time.LocalDateTime.now

class TestItemFactory {

    static Item itemAlreadyFinished(Long lender) {
        def item = new Item(
                description: "Drill",
                rentDuration: ofDays(5),
                account: accountOf(lender))
        def order = new Order(item, 2)
        order.id = 1L
        order.status = OrderStatus.FINISHED
        item.addOrder(order)
        item
    }

    static Item rentedDrillWith(Long lender, Long itemId = 1) {
        def item = new Item(
                id: itemId,
                description: "Drill",
                rentDuration: ofDays(5),
                account: accountOf(lender))
        def order = new Order(item, 2)
        order.id = 1L
        item.addOrder(order)
        item
    }

    static Item availableDrillWith(Long lender) {
        Duration rentDuration = ofDays(2)
        return new Item(
                id: 1001,
                rentDuration: rentDuration,
                assuranceCost: valueOf(1),
                price: valueOf(10L),
                description: "Drill",
                account: accountOf(lender))
    }

    static Item availablePS5(Long lender) {
        Duration rentDuration = ofDays(2)
        return new Item(
                id: 1002,
                rentDuration: rentDuration,
                assuranceCost: valueOf(1),
                price: valueOf(500L),
                description: "PS5",
                account: accountOf(lender))
    }

    private static Account accountOf(Long accountId) {
        return new Account(id: accountId, email: "jocabrera@fi.uba.com.ar")
    }
}
