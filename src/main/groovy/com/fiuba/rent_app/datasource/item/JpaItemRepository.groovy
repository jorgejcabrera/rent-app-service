package com.fiuba.rent_app.datasource.item

import com.fiuba.rent_app.domain.item.Item
import org.springframework.data.repository.CrudRepository

interface JpaItemRepository extends CrudRepository<Item, Long> {
    Item save(Item item)

    List<Item> findAll()

    Optional<Item> findById(Long id)
}