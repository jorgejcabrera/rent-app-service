package com.fiuba.rent_app.datasource

import com.fiuba.rent_app.domain.item.Item
import org.springframework.data.repository.CrudRepository

interface JpaItemRepository extends CrudRepository<Item, UUID> {
    Item save(Item item)

    List<Item> findAll()
}