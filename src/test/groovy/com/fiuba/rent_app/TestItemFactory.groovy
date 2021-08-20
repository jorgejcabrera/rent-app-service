package com.fiuba.rent_app

import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item

import java.time.Duration

import static com.fiuba.rent_app.domain.item.ItemStatus.AVAILABLE
import static com.fiuba.rent_app.domain.item.ItemStatus.RENTED
import static java.math.BigDecimal.valueOf
import static java.time.Duration.ofDays

class TestItemFactory {

    static Item rentedDrillWith(Long lender) {
        return new Item(
                status: RENTED,
                description: "Drill",
                rentDuration: ofDays(1),
                account: accountOf(lender))
    }

    static Item availableDrillWith(Long lender) {
        Duration rentDuration = ofDays(2)
        return new Item(
                status: AVAILABLE,
                rentDuration: rentDuration,
                price: valueOf(10L),
                description: "Drill",
                account: accountOf(lender))
    }

    private static Account accountOf(Long accountId) {
        return new Account(id: accountId, email: "jocabrera@fi.uba.com.ar")
    }
}
