package org.fiuba.domain;

import org.fiuba.presentation.ItemCreationBody;

public class ItemServiceAdapter implements ItemService {

    private ItemRepository repository;
    private ItemBuilder builder;

    public ItemServiceAdapter(
            ItemRepository repository,
            ItemBuilder builder
    ) {
        this.repository = repository;
        this.builder = builder;
    }

    @Override
    public Item create(ItemCreationBody body, Long renterId) {
        Item item = builder
                .price(body.price)
                .rentDaysDuration(body.rentDaysDuration)
                .description(body.description)
                .renter(renterId)
                .build();
        return repository.save(item);
    }
}
