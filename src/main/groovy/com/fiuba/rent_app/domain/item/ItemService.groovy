package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.presentation.item.ItemCreationBody

interface ItemService {
    Item create(ItemCreationBody body, Long renterId)

    List<Item> listAll()
}