package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.service.ItemService
import com.fiuba.rent_app.domain.item.service.ItemServiceAdapter
import com.fiuba.rent_app.presentation.item.ItemCreationBody
import org.jetbrains.annotations.NotNull
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
    private JpaItemRepository repository

    @Mock
    private JpaAccountRepository accountRepository
    private ItemService service

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ItemServiceAdapter(itemRepository: repository, accountRepository: accountRepository)
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

    private void theItemWasSaved() {
        verify(repository, times(1)).save(any())
    }

    @NotNull
    private static ItemCreationBody givenAItemCreationBody() {
        return new ItemCreationBody("", BigDecimal.valueOf(10L), 1)
    }

    void givenAnAccount() {
        whenever(accountRepository.findById(any())).thenReturn(Optional.of(new Account(id: 1L, email: "jocabrera@fi.uba.ar")))
    }
}