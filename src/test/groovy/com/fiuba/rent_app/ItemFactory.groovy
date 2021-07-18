package com.fiuba.rent_app

import com.fiuba.rent_app.domain.item.Item

import java.time.Duration

import static com.fiuba.rent_app.domain.item.ItemStatus.AVAILABLE
import static java.math.BigDecimal.valueOf
import static java.time.Duration.ofDays

class ItemFactory {

    static Item availableDrillWith(Long borrowerId) {
        Duration rentDuration = ofDays(2)
        return new Item(
                status: AVAILABLE,
                rentDuration: rentDuration,
                price: valueOf(10L),
                description: "Drill",
                borrower: borrowerId)
    }
}
