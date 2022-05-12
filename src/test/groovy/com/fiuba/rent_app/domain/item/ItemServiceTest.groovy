package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.TestItemFactory
import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.account.Account
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
    void when_an_item_has_been_created_then_the_amount_to_pay_will_be_the_sum_of_the_price_and_assurance_amount() {
        // GIVEN
        givenAnAccount()
        ItemCreationBody body = givenAItemCreationBody()

        // WHEN
        Item item = service.create(body, 1L)

        // THEN
        Assertions.assertEquals(body.assuranceCost + body.price, item.totalToPay)
    }

    @Test
    void when_the_service_creates_an_item_then_the_repository_must_be_used_to_save_it() {
        // GIVEN
        givenAnAccount()
        ItemCreationBody body = givenAItemCreationBody()

        // WHEN
        service.create(body, 1L)

        // THEN
        theItemWasSaved()
    }

    @Test
    void when_list_all_the_items_then_only_the_available_items_must_be_retrieved() {
        // GIVEN
        givenSomeSavedItems()

        // WHEN
        def items = service.listAll()

        // THEN
        Assertions.assertEquals(2, items.size())
    }

    @Test
    void when_republish_an_item_with_orders_then_all_of_those_must_be_closed() {
        // GIVEN
        givenARentedItem()
        ItemRepublishingBody body = new ItemRepublishingBody(price: 10.0, rentingDays: 2)

        // WHEN
        def item = service.republish(body, 1)

        // THEN
        Assertions.assertFalse(item.isBeingUsed())
        assertTrue(item.orders.find { it.isOpen() } == null)
    }

    @Test
    void when_republish_an_item_that_does_not_have_any_orders_then_it_must_work_ok() {
        // GIVEN
        givenACommonItem()
        ItemRepublishingBody body = new ItemRepublishingBody(price: 10.0, rentingDays: 2)

        // WHEN
        def item = service.republish(body, 1)

        // THEN
        Assertions.assertFalse(item.isBeingUsed())
        assertTrue(item.orders.find { it.isOpen() } == null)
    }

    private void theItemWasSaved() {
        verify(itemRepository, times(1)).save(any())
    }

    @NotNull
    private static ItemCreationBody givenAItemCreationBody() {
        return new ItemCreationBody(
                description: "",
                price: BigDecimal.valueOf(10L),
                rentingDays: 1,
                assuranceCost: BigDecimal.valueOf(100)
        )
    }

    void givenAnAccount() {
        whenever(accountRepository.findById(any())).thenReturn(Optional.of(new Account(id: 1L, email: "jocabrera@fi.uba.ar")))
    }

    def givenSomeSavedItems() {
        List<Item> items = [
                TestItemFactory.rentedDrillWith(1),
                new Item(title: "play station 5"),
                new Item(title: "computadora")
        ]
        whenever(itemRepository.findAll()).thenReturn(items)
    }

    void givenARentedItem() {
        def rentedItem = TestItemFactory.rentedDrillWith(1)
        whenever(itemRepository.findById(any())).thenReturn(Optional.of(rentedItem))
    }

    void givenACommonItem() {
        def commonItem = TestItemFactory.availableDrillWith(1)
        whenever(itemRepository.findById(any())).thenReturn(Optional.of(commonItem))
    }
}