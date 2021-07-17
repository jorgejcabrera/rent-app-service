package com.fiuba.rent_app.datasource.item

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

    @Override
    List<Item> findAll() {
        return jpaItemRepository.findAll()
    }

    @Override
    Item findById(Long itemId) {
        return jpaItemRepository.findById(itemId).orElseThrow { new ItemNotFoundException("Item $itemId does not exist") }
    }
}
