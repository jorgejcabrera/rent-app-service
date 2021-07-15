package com.fiuba.rent_app.datasource

import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.ItemRepository

class ItemRepositoryAdapter implements ItemRepository {

    private JpaItemRepository jpaItemRepository

    ItemRepositoryAdapter(JpaItemRepository jpaItemRepository) {
        this.jpaItemRepository = jpaItemRepository
    }

    @Override
    Item save(Item item) {
        return jpaItemRepository.save(item)
    }
}
