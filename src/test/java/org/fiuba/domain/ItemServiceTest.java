package org.fiuba.domain;

import org.fiuba.domain.item.ItemBuilderAdapter;
import org.fiuba.domain.item.ItemRepository;
import org.fiuba.domain.item.ItemService;
import org.fiuba.domain.item.ItemServiceAdapter;
import org.fiuba.presentation.ItemCreationBody;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ItemServiceTest {

    @Mock
    private ItemRepository repository;
    private ItemService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ItemServiceAdapter(repository, new ItemBuilderAdapter());
    }

    @Test
    public void when_the_service_create_an_item_then_it_must_use_the_repository() {
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