package com.fiuba.rent_app.domain.item

import com.fiuba.rent_app.presentation.ItemCreationBody

interface ItemService {
    Item create(ItemCreationBody body, Long renterId);
}