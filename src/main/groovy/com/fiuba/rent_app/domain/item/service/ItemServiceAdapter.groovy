package com.fiuba.rent_app.domain.item.service

import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.builder.ItemBuilderAdapter
import com.fiuba.rent_app.presentation.item.ItemCreationBody

class ItemServiceAdapter implements ItemService {

    private JpaItemRepository itemRepository

    private JpaAccountRepository accountRepository

    @Override
    Item create(ItemCreationBody body, Long borrowerId) {
        Account borrower = accountRepository.findById(borrowerId)
                .orElseThrow { new ItemBorrowerDoesNotExistException("The account $borrowerId does not exist.") }
        Item item = new ItemBuilderAdapter()
                .price(body.price)
                .rentDaysDuration(body.rentingDays)
                .description(body.description)
                .borrower(borrower)
                .build()
        return itemRepository.save(item)
    }

    @Override
    List<Item> listAll() {
        return itemRepository.findAll()
    }
}
