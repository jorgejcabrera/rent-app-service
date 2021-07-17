package com.fiuba.rent_app.domain.item


import com.fiuba.rent_app.presentation.item.ItemCreationBody

class ItemServiceAdapter implements ItemService {

    private final ItemRepository repository

    ItemServiceAdapter(
            ItemRepository repository
    ) {
        this.repository = repository
    }

    @Override
    Item create(ItemCreationBody body, Long renterId) {
        Item item = new ItemBuilderAdapter()
                .price(body.price)
                .rentDaysDuration(body.rentingDays)
                .description(body.description)
                .renter(renterId)
                .build()
        return repository.save(item)
    }

    @Override
    List<Item> listAll() {
        return repository.findAll()
    }
}
