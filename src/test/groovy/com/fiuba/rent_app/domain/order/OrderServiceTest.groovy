package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.TestItemFactory
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.datasource.order.JpaOrderRepository
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.builder.OrderBuilderImpl
import com.fiuba.rent_app.domain.order.builder.exception.InvalidRenterException
import com.fiuba.rent_app.domain.order.builder.exception.ItemIsNotAvailableForOrderingException
import com.fiuba.rent_app.domain.order.service.ItemNotFoundException
import com.fiuba.rent_app.domain.order.service.OrderService
import com.fiuba.rent_app.domain.order.service.OrderServiceAdapter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.time.Duration
import java.time.LocalDateTime

import static com.nhaarman.mockitokotlin2.OngoingStubbingKt.whenever
import static org.junit.jupiter.api.Assertions.*
import static org.mockito.ArgumentMatchers.any

class OrderServiceTest {

    @Mock
    private JpaItemRepository itemRepository

    @Mock
    private JpaOrderRepository orderRepository

    private OrderService service

    Long itemId = 1L
    Long borrowerId = 2L
    Long lenderId = 1L
    Item drill = TestItemFactory.availableDrillWith(lenderId)
    Duration rentDuration = drill.rentDuration

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this)
        service = new OrderServiceAdapter(
                itemRepository: itemRepository,
                orderRepository: orderRepository
        )
    }

    @Test
    void when_create_an_order_for_a_nonexistent_item_then_it_must_throw_an_exception() {
        // GIVEN
        givenAnNoneExistentItem()

        // THEN
        assertThrows(ItemNotFoundException.class) { service.createFor(itemId, borrowerId) }
    }

    @Test
    void when_try_to_order_a_rented_item_then_it_must_throw_an_exception() {
        // GIVEN
        givenARentedItem()

        // THEN
        assertThrows(ItemIsNotAvailableForOrderingException.class) { service.createFor(itemId, borrowerId) }
    }

    @Test
    void when_ordering_an_available_item_then_a_valid_order_must_be_returned() {
        // GIVEN
        givenAnAvailableItem()
        givenAnOrderSuccessfullySaved()

        // WHEN
        Order createdOrder = service.createFor(itemId, borrowerId)

        // THEN
        thenTheOrderWasSuccessfullyCreated(createdOrder)
    }

    @Test
    void when_ordering_an_available_item_then_the_returning_date_must_be_indicated_in_the_order_info() {
        // GIVEN
        givenAnAvailableItem()
        givenAnOrderSuccessfullySaved()

        // WHEN
        Order createdOrder = service.createFor(itemId, borrowerId)

        // THEN
        thenTheExpiredRentDateWasSuccessfullyCalculated(createdOrder)
    }

    @Test
    void when_the_borrower_is_the_item_owner_then_it_must_thrown_an_exception() {
        // GIVEN
        givenAnAvailableItem()

        // WHEN
        assertThrows(InvalidRenterException.class) {
            service.createFor(itemId, lenderId)
        }
    }

    void givenAnNoneExistentItem() {
        whenever(itemRepository.findById(itemId)).thenThrow(new ItemNotFoundException("Item does not exist"))
    }

    void givenARentedItem() {
        whenever(itemRepository.findById(itemId)).thenReturn(Optional.of(TestItemFactory.rentedDrillWith(lenderId)))
    }

    void givenAnAvailableItem() {
        whenever(itemRepository.findById(itemId)).thenReturn(Optional.of(drill))
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

    void givenAnOrderSuccessfullySaved() {
        whenever(orderRepository.save(any())).thenReturn(new OrderBuilderImpl().item(drill).borrowerId(borrowerId).build())
    }
}
