package com.fiuba.rent_app


import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.Order
import com.fiuba.rent_app.domain.order.OrderStatus

import java.time.Duration
import java.time.LocalDateTime

import static com.fiuba.rent_app.AccountFactory.*
import static java.math.BigDecimal.valueOf
import static java.time.Duration.ofDays

class TestItemFactory {


    static Item withExpiredOrderBy(Long debtorId = BORROWER.id) {
        def item = new Item(
                id: 1,
                description: "Drill",
                rentDuration: ofDays(5),
                account: LENDER)
        def order = new Order(
                id: 1,
                item: item,
                rentDay: LocalDateTime.now().minusDays(10),
                status: OrderStatus.OPEN,
                borrower: debtorId
        )
        item.addOrder(order)
        item
    }

    static Item withOrderAlreadyFinished(Long lender) {
        def item = new Item(
                description: "Drill",
                rentDuration: ofDays(5),
                account: of(lender))
        def order = new Order(item, BORROWER)
        order.id = 1L
        order.rentDay = LocalDateTime.now().minusDays(10)
        order.status = OrderStatus.FINISHED
        item.addOrder(order)
        item
    }

    static Item rentedDrillBy(Long itemId = 1) {
        def item = new Item(
                id: itemId,
                description: "Drill",
                rentDuration: ofDays(5),
                account: of(1))
        def order = new Order(item, BORROWER)
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
                account: of(lender))
    }

    static Item availablePS5(Long lender) {
        Duration rentDuration = ofDays(2)
        return new Item(
                id: 1002,
                rentDuration: rentDuration,
                assuranceCost: valueOf(1),
                price: valueOf(500L),
                description: "PS5",
                account: of(lender))
    }


}
