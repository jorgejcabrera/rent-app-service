package com.fiuba.rent_app.domain.item

interface ItemRepository {
    Item save(Item item)

    List<Item> findAll()

    Item findById(UUID itemId)
}