package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.TestItemFactory
import com.fiuba.rent_app.domain.item.exception.ItemInUseException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import java.time.Duration

class ItemTest {


    @Test
    void "item with expired orders"() {
        // GIVEN
        Item item = TestItemFactory.withExpiredOrderBy()

        // THEN
        Assertions.assertTrue(item.hasExpiredOrders())
    }

    @Test
    void "item without expired orders"() {
        // GIVEN
        Item item = TestItemFactory.withOrderAlreadyFinished(1)

        // THEN
        Assertions.assertFalse(item.hasExpiredOrders())
    }

    @Test
    void "when an item is being used, then it can not be modified"() {
        // GIVEN
        Item rentedItem = TestItemFactory.rentedDrillBy()

        // THEN
        Assertions.assertThrows(ItemInUseException.class) {
            rentedItem.update(BigDecimal.valueOf(1), Duration.ofDays(1))
        }
    }
}
