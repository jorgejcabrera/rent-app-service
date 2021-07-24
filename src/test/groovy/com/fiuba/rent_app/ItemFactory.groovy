package com.fiuba.rent_app

import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item

import java.time.Duration

import static com.fiuba.rent_app.domain.item.ItemStatus.AVAILABLE
import static com.fiuba.rent_app.domain.item.ItemStatus.RENTED
import static java.math.BigDecimal.valueOf
import static java.time.Duration.ofDays

class ItemFactory {

    static Item rentedDrillWith(Long borrowerId) {
        return new Item(
                status: RENTED,
                description: "Drill",
                rentDuration: ofDays(1),
                account: accountOf(borrowerId))
    }

    static Item availableDrillWith(Long borrowerId) {
        Duration rentDuration = ofDays(2)
        return new Item(
                status: AVAILABLE,
                rentDuration: rentDuration,
                price: valueOf(10L),
                description: "Drill",
                account: accountOf(borrowerId))
    }

    private static Account accountOf(Long borrowerId) {
        return new Account(id: borrowerId, email: "jocabrera@fi.uba.com.ar")
    }
}
