package com.fiuba.rent_app.domain.item.service

import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.presentation.item.ItemCreationBody

interface ItemService {
    Item create(ItemCreationBody body, Long lenderId)

    List<Item> listAll()
}