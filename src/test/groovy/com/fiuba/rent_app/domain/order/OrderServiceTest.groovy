package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.TestItemFactory
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.datasource.order.JpaOrderRepository
import com.fiuba.rent_app.domain.item.Item

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
    void "when create an order for a nonexistent item then it must throw an exception"() {
        // GIVEN
        givenAnNoneExistentItem()

        // THEN
        assertThrows(ItemNotFoundException.class) { service.createFor(itemId, borrowerId) }
    }

    @Test
    void "when try to order a rented item then it must throw an exception"() {
        // GIVEN
        givenARentedItem()

        // THEN
        assertThrows(ItemIsNotAvailableForOrderingException.class) { service.createFor(itemId, borrowerId) }
    }

    @Test
    void "when ordering an available item then a valid order must be returned"() {
        // GIVEN
        givenAnAvailableItem()
        givenAnOrderSuccessfullySaved()

        // WHEN
        Order createdOrder = service.createFor(itemId, borrowerId)

        // THEN
        thenTheOrderWasSuccessfullyCreated(createdOrder)
    }

    @Test
    void "when ordering an available item then the creation date must be indicated in the order info"() {
        // GIVEN
        givenAnAvailableItem()
        givenAnOrderSuccessfullySaved()

        // WHEN
        Order createdOrder = service.createFor(itemId, borrowerId)

        // THEN
        thenTheExpiredRentDateWasSuccessfullyCalculated(createdOrder)
    }

    @Test
    void "when the borrower is the item owner then it must thrown an exception"() {
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
        assertEquals(expectedDate.dayOfMonth, order.item.expireRentDay().dayOfMonth)
        assertEquals(expectedDate.month, order.item.expireRentDay().month)
        assertEquals(expectedDate.year, order.item.expireRentDay().year)
    }

    void givenAnOrderSuccessfullySaved() {
        whenever(orderRepository.save(any())).thenReturn(new Order(drill, borrowerId))

    }
}
