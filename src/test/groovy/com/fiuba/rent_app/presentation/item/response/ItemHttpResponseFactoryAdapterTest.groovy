package com.fiuba.rent_app.presentation.item.response

import com.fiuba.rent_app.TestItemFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

import static org.junit.jupiter.api.Assertions.assertEquals

class ItemHttpResponseFactoryAdapterTest {

    ItemHttpResponseFactory responseFactory;

    @BeforeEach
    void setUp() {
        responseFactory = new ItemHttpResponseFactoryAdapter();
    }

    @Test
    void when_list_all_items_then_the_response_must_be_created_ok() {
        // GIVEN
        def items = [TestItemFactory.availablePS5(1), TestItemFactory.availableDrillWith(2)]

        // WHEN
        def response = responseFactory.from(items)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(2, response.body.size())
    }

}