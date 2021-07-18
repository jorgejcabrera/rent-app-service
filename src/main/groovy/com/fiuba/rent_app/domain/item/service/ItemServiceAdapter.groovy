package com.fiuba.rent_app.domain.item.service

import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.builder.ItemBuilderAdapter
import com.fiuba.rent_app.presentation.item.ItemCreationBody

class ItemServiceAdapter implements ItemService {

    private JpaItemRepository itemRepository

    @Override
    Item create(ItemCreationBody body, Long renterId) {
        Item item = new ItemBuilderAdapter()
                .price(body.price)
                .rentDaysDuration(body.rentingDays)
                .description(body.description)
                .renter(renterId)
                .build()
        return itemRepository.save(item)
    }

    @Override
    List<Item> listAll() {
        return itemRepository.findAll()
    }
}
