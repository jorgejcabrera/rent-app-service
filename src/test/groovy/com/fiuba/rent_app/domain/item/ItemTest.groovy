package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.TestItemFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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
}
