package com.fiuba.rent_app.domain.item;

import com.fiuba.rent_app.datasource.item.JpaItemRepository;
import com.fiuba.rent_app.domain.item.service.ItemService;
import com.fiuba.rent_app.domain.item.service.ItemServiceAdapter;
import com.fiuba.rent_app.presentation.item.ItemCreationBody;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static com.nhaarman.mockitokotlin2.VerificationKt.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class ItemServiceTest {

    @Mock
    private JpaItemRepository repository;
    private ItemService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ItemServiceAdapter(itemRepository: repository);
    }

    @Test
    void when_the_service_creates_an_item_then_the_repository_must_be_used_to_save_it() {
        // GIVEN
        ItemCreationBody body = givenAItemCreationBody();

        // WHEN
        service.create(body, 1L);

        // THEN
        theItemWasSaved();
    }

    private void theItemWasSaved() {
        verify(repository, times(1)).save(any());
    }

    @NotNull
    private ItemCreationBody givenAItemCreationBody() {
        return new ItemCreationBody("", BigDecimal.valueOf(10L), 1);
    }

}