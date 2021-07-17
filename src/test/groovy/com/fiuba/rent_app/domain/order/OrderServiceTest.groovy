package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.datasource.item.ItemNotFoundException
import com.fiuba.rent_app.datasource.order.JpaOrderRepository
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.ItemRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.time.Duration
import java.time.LocalDateTime

import static com.fiuba.rent_app.domain.item.ItemStatus.AVAILABLE
import static com.fiuba.rent_app.domain.item.ItemStatus.RENTED
import static com.nhaarman.mockitokotlin2.OngoingStubbingKt.whenever
import static java.math.BigDecimal.valueOf
import static java.time.Duration.ofDays
import static org.junit.jupiter.api.Assertions.*

class OrderServiceTest {

    @Mock
    private ItemRepository itemRepository

    @Mock
    private JpaOrderRepository jpaOrderRepository

    private OrderService service

    Long itemId = 1L
    Long renterId = 1L
    Duration rentDuration = ofDays(2)
    Item taladro = new Item(
            status: AVAILABLE,
            rentDuration: rentDuration,
            price: valueOf(10L),
            description: "taladro")

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this)
        service = new OrderServiceAdapter(itemRepository, jpaOrderRepository)
    }

    @Test
    void when_create_an_order_for_a_nonexistent_item_then_it_must_throw_an_exception() {
        // GIVEN
        givenAnNoneExistentItem()

        // THEN
        assertThrows(ItemNotFoundException.class) { service.createFor(itemId, renterId) }
    }

    @Test
    void when_try_to_order_a_rented_item_then_it_must_throw_an_exception() {
        // GIVEN
        givenARentedItem()

        // THEN
        assertThrows(ItemIsNotAvailableForOrderingException.class) { service.createFor(itemId, renterId) }
    }

    @Test
    void when_ordering_an_available_item_then_the_a_valid_order_must_be_returned() {
        // GIVEN
        givenAnAvailableItem()

        // WHEN
        Order createdOrder = service.createFor(itemId, renterId)

        // THEN
        thenTheOrderWasSuccessfullyCreated(createdOrder)
    }

    @Test
    void when_ordering_an_available_item_then_the_returning_date_must_be_indicated_in_the_order_info() {
        // GIVEN
        givenAnAvailableItem()

        // WHEN
        Order createdOrder = service.createFor(itemId, renterId)

        // THEN
        thenTheExpiredRentDateWasSuccessfullyCalculated(createdOrder)
    }


    void givenAnNoneExistentItem() {
        whenever(itemRepository.findById(itemId)).thenThrow(new ItemNotFoundException("Item does not exist"))
    }

    void givenARentedItem() {
        whenever(itemRepository.findById(itemId)).thenReturn(new Item(status: RENTED))
    }

    void givenAnAvailableItem() {
        whenever(itemRepository.findById(itemId)).thenReturn(taladro)
    }

    static void thenTheOrderWasSuccessfullyCreated(Order order) {
        assertNotNull(order)
    }

    void thenTheExpiredRentDateWasSuccessfullyCalculated(Order order) {
        LocalDateTime expectedDate = LocalDateTime.now() + rentDuration
        assertEquals(expectedDate.dayOfMonth, order.expiredRentDay.dayOfMonth)
        assertEquals(expectedDate.month, order.expiredRentDay.month)
        assertEquals(expectedDate.year, order.expiredRentDay.year)
    }
}
