package com.fiuba.rent_app.domain.order

import com.fiuba.rent_app.TestItemFactory
import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.datasource.order.JpaOrderRepository
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.exception.InvalidCallerException
import com.fiuba.rent_app.domain.order.exception.ItemIsNotAvailableForOrderingException
import com.fiuba.rent_app.domain.order.exception.ItemNotFoundException
import com.fiuba.rent_app.domain.order.service.OrderService
import com.fiuba.rent_app.domain.order.service.OrderServiceAdapter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.time.Duration
import java.time.LocalDateTime

import static com.fiuba.rent_app.AccountFactory.*
import static com.nhaarman.mockitokotlin2.OngoingStubbingKt.whenever
import static org.junit.jupiter.api.Assertions.*
import static org.mockito.ArgumentMatchers.any

class OrderServiceTest {

    @Mock
    private JpaItemRepository itemRepository

    @Mock
    private JpaOrderRepository orderRepository

    @Mock
    private JpaAccountRepository accountRepository

    private OrderService service

    Long itemId = 1L
    Long lenderId = 1L
    Item drill = TestItemFactory.availableDrillWith(lenderId)
    Duration rentDuration = drill.rentDuration

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this)
        service = new OrderServiceAdapter(
                itemRepository: itemRepository,
                orderRepository: orderRepository,
                accountRepository: accountRepository
        )
    }

    @Test
    void "when a debtor try to create an order, then an exception must be thrown"() {
        // GIVEN
        whenever(accountRepository.findById(any())).thenReturn(Optional.of(DEBTOR))
        givenAnAvailableItem()

        // WHEN
        assertThrows(InvalidCallerException.class) {
            service.createFor(itemId, DEBTOR.id)
        }
    }


    @Test
    void "when creates an order, then expired rent day must be set ok"() {
        // GIVEN
        whenever(accountRepository.findById(any())).thenReturn(Optional.of(BORROWER))
        Item item = givenAnAvailableItem()

        // WHEN
        Order order = service.createFor(itemId, BORROWER.id)

        // THEN
        assertNotNull(order.expireRentDay())
        assertTrue(LocalDateTime.now() < order.expireRentDay())

    }

    @Test
    void "when creates an order for a nonexistent item then it must throw an exception"() {
        // GIVEN
        whenever(accountRepository.findById(any())).thenReturn(Optional.of(BORROWER))
        givenAnNoneExistentItem()

        // THEN
        assertThrows(ItemNotFoundException.class) { service.createFor(itemId, BORROWER.id) }
    }

    @Test
    void "when try to order a rented item then it must throw an exception"() {
        // GIVEN
        whenever(accountRepository.findById(any())).thenReturn(Optional.of(BORROWER))
        givenARentedItem()

        // THEN
        assertThrows(ItemIsNotAvailableForOrderingException.class) { service.createFor(itemId, BORROWER.id) }
    }

    @Test
    void "when ordering an available item then a valid order must be returned"() {
        // GIVEN
        whenever(accountRepository.findById(any())).thenReturn(Optional.of(BORROWER))
        givenAnAvailableItem()
        givenAnOrderSuccessfullySaved()

        // WHEN
        Order createdOrder = service.createFor(itemId, BORROWER.id)

        // THEN
        thenTheOrderWasSuccessfullyCreated(createdOrder)
    }

    @Test
    void "when ordering an available item then the creation date must be indicated in the order info"() {
        // GIVEN
        whenever(accountRepository.findById(any())).thenReturn(Optional.of(BORROWER))
        givenAnAvailableItem()
        givenAnOrderSuccessfullySaved()

        // WHEN
        Order createdOrder = service.createFor(itemId, BORROWER.id)

        // THEN
        thenTheExpiredRentDateWasSuccessfullyCalculated(createdOrder)
    }

    @Test
    void "when the borrower is the item owner then it must thrown an exception"() {
        // GIVEN
        givenAnAvailableItem()

        // WHEN
        assertThrows(InvalidCallerException.class) {
            service.createFor(itemId, LENDER.id)
        }
    }

    void givenAnNoneExistentItem() {
        whenever(itemRepository.findById(itemId)).thenThrow(new ItemNotFoundException("Item does not exist"))
    }

    void givenARentedItem() {
        whenever(itemRepository.findById(itemId)).thenReturn(Optional.of(TestItemFactory.rentedDrillWith()))
    }

    Item givenAnAvailableItem() {
        whenever(itemRepository.findById(itemId)).thenReturn(Optional.of(drill))
        return drill
    }

    static void thenTheOrderWasSuccessfullyCreated(Order order) {
        assertNotNull(order)
    }

    void thenTheExpiredRentDateWasSuccessfullyCalculated(Order order) {
        LocalDateTime expectedDate = LocalDateTime.now() + rentDuration
        assertEquals(expectedDate.dayOfMonth, order.expireRentDay().dayOfMonth)
        assertEquals(expectedDate.month, order.expireRentDay().month)
        assertEquals(expectedDate.year, order.expireRentDay().year)
    }

    void givenAnOrderSuccessfullySaved() {
        whenever(orderRepository.save(any())).thenReturn(new Order(drill, BORROWER))
    }
}
