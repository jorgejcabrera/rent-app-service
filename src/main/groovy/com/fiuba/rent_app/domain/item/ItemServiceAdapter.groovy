package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.presentation.ItemCreationBody

class ItemServiceAdapter implements ItemService {

    private final ItemRepository repository
    private final ItemBuilder builder

    ItemServiceAdapter(
            ItemRepository repository,
            ItemBuilder builder
    ) {
        this.repository = repository
        this.builder = builder
    }

    @Override
    Item create(ItemCreationBody body, Long renterId) {
        Item item = builder
                .price(body.price)
                .rentDaysDuration(body.rentDaysDuration)
                .description(body.description)
                .renter(renterId)
                .build()
        return repository.save(item)
    }
}
