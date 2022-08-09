package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.TestItemFactory
import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.exception.InvalidLenderIdException
import com.fiuba.rent_app.domain.item.exception.ItemInUseException
import com.fiuba.rent_app.domain.item.service.ItemService
import com.fiuba.rent_app.domain.item.service.ItemServiceImpl
import com.fiuba.rent_app.presentation.item.ItemCreationBody
import com.fiuba.rent_app.presentation.item.ItemRepublishingBody
import org.jetbrains.annotations.NotNull
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static com.nhaarman.mockitokotlin2.OngoingStubbingKt.whenever
import static com.nhaarman.mockitokotlin2.VerificationKt.verify
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.times

class ItemServiceTest {

    @Mock
    private JpaItemRepository itemRepository

    @Mock
    private JpaAccountRepository accountRepository
    private ItemService service

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ItemServiceImpl(itemRepository: itemRepository, accountRepository: accountRepository)
    }


    @Test
    void "when an item has been created then the amount to pay will be the sum of the price and assurance amount"() {
        // GIVEN
        givenAnAccount()
        ItemCreationBody body = givenAItemCreationBody()

        // WHEN
        Item item = service.create(body, 1L)

        // THEN
        Assertions.assertEquals(body.assuranceCost + body.price, item.totalToPay)
    }

    @Test
    void "when the service creates an item then the repository must be used to save it"() {
        // GIVEN
        givenAnAccount()
        ItemCreationBody body = givenAItemCreationBody()

        // WHEN
        service.create(body, 1L)

        // THEN
        theItemWasSaved()
    }

    @Test
    void "when list all the items then only the available items must be retrieved"() {
        // GIVEN
        givenSomeSavedItems()

        // WHEN
        def items = service.listAll()

        // THEN
        Assertions.assertEquals(2, items.size())
    }

    @Test
    void "when republish an item with an open order, then an exception must be thrown"() {
        // GIVEN
        givenARentedItem()
        ItemRepublishingBody body = new ItemRepublishingBody(price: 10.0, rentingDays: 2)

        // WHEN
        assertThrows(ItemInUseException.class) { service.update(body, 1) }
    }

    @Test
    void "when republish an item that does not have any orders then it must work ok"() {
        // GIVEN
        givenACommonItem()
        ItemRepublishingBody body = new ItemRepublishingBody(price: 10.0, rentingDays: 2)

        // WHEN
        def item = service.update(body, 1)

        // THEN
        Assertions.assertFalse(item.isBeingUsed())
        assertTrue(item.orders.find { it.isOpen() } == null)
    }

    @Test
    void "when republish an item associated with an order already closed, then it must be available"() {
        // GIVEN
        givenAnItemWithAnOrderFinished()
        ItemRepublishingBody body = new ItemRepublishingBody(price: 10.0, rentingDays: 2)

        // WHEN
        Item item = service.update(body, 1)

        // THEN
        Assertions.assertFalse(item.isBeingUsed())

    }

    @Test
    void "when return a rented item, then it can be rented again"() {
        // GIVEN
        Long borrower = 2
        Long itemId = 1
        givenARentedItem(borrower, itemId)

        // WHEN
        Item item = service.free(itemId, borrower)

        // THEN
        Assertions.assertTrue(item.canBeRented())
    }

    @Test
    void "when try to return a rented item by someone different of the lender, then an error must be thrown"() {
        // GIVEN
        Long borrowerId = 101
        Long itemId = 1
        givenARentedItem(2, itemId)

        // WHEN
        assertThrows(InvalidLenderIdException.class) {
            service.free(itemId, borrowerId)
        }
    }

    private void theItemWasSaved() {
        verify(itemRepository, times(1)).save(any())
    }

    @NotNull
    private static ItemCreationBody givenAItemCreationBody() {
        return new ItemCreationBody(
                title: "My item",
                description: "",
                price: BigDecimal.valueOf(10L),
                rentingDays: 1,
                assuranceCost: BigDecimal.valueOf(100)
        )
    }

    void givenAnAccount() {
        whenever(accountRepository.findById(any())).thenReturn(Optional.of(new Account(id: 1L, email: "jocabrera@fi.uba.ar")))
    }

    def givenSomeSavedItems(Long borrowerId = 2) {
        List<Item> items = [
                TestItemFactory.rentedDrillBy(),
                new Item(title: "play station 5"),
                new Item(title: "computadora")
        ]
        whenever(itemRepository.findAll()).thenReturn(items)
    }

    void givenARentedItem(Long borrowerId = 2, Long itemId = 1) {
        def rentedItem = TestItemFactory.rentedDrillBy(itemId)
        whenever(itemRepository.findById(any())).thenReturn(Optional.of(rentedItem))
    }

    void givenACommonItem() {
        def commonItem = TestItemFactory.availableDrillWith(1)
        whenever(itemRepository.findById(any())).thenReturn(Optional.of(commonItem))
    }

    void givenAnItemWithAnOrderFinished() {
        def itemAlreadyFinished = TestItemFactory.withOrderAlreadyFinished(1)
        whenever(itemRepository.findById(any())).thenReturn(Optional.of(itemAlreadyFinished))
    }
}