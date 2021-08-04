package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.service.ItemService
import com.fiuba.rent_app.domain.item.service.ItemServiceAdapter
import com.fiuba.rent_app.presentation.item.ItemCreationBody
import org.jetbrains.annotations.NotNull
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static com.nhaarman.mockitokotlin2.OngoingStubbingKt.whenever
import static com.nhaarman.mockitokotlin2.VerificationKt.verify
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
        service = new ItemServiceAdapter(itemRepository: itemRepository, accountRepository: accountRepository)
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

    private void theItemWasSaved() {
        verify(itemRepository, times(1)).save(any())
    }

    @NotNull
    private static ItemCreationBody givenAItemCreationBody() {
        return new ItemCreationBody("", BigDecimal.valueOf(10L), 1)
    }

    void givenAnAccount() {
        whenever(accountRepository.findById(any())).thenReturn(Optional.of(new Account(id: 1L, email: "jocabrera@fi.uba.ar")))
    }

    def givenSomeSavedItems() {
        List<Item> items = [
                new Item(title: "zapatilla", status: ItemStatus.RENTED),
                new Item(title: "play station 5"),
                new Item(title: "computadora")
        ]
        whenever(itemRepository.findAll()).thenReturn(items)
    }
}