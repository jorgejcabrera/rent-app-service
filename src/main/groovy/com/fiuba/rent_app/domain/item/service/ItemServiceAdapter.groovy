package com.fiuba.rent_app.domain.item.service

import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.builder.ItemBuilderAdapter
import com.fiuba.rent_app.presentation.item.ItemCreationBody

import java.util.stream.Collectors

class ItemServiceAdapter implements ItemService {

    private JpaItemRepository itemRepository

    private JpaAccountRepository accountRepository

    @Override
    Item create(ItemCreationBody body, Long lenderId) {
        Account lender = accountRepository.findById(lenderId)
                .orElseThrow { new ItemLenderDoesNotExistException("The account $lenderId does not exist.") }
        Item item = new ItemBuilderAdapter()
                .price(body.price)
                .rentDaysDuration(body.rentingDays)
                .description(body.description)
                .assuranceCost(body.assuranceCost)
                .lender(lender)
                .title(body.title)
                .build()
        itemRepository.save(item)
        return item
    }

    @Override
    List<Item> listAll() {
        itemRepository.findAll()
                .stream()
                .filter { !it.isBeingUsed() }
                .collect(Collectors.toList())
    }
}
