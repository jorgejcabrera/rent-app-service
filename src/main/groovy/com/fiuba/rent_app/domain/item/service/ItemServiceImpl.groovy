package com.fiuba.rent_app.domain.item.service

import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.builder.ItemBuilderImpl
import com.fiuba.rent_app.presentation.item.ItemCreationBody
import com.fiuba.rent_app.presentation.item.ItemRepublishingBody

import java.time.Duration
import java.util.stream.Collectors

import static java.time.Duration.*

class ItemServiceImpl implements ItemService {

    private JpaItemRepository itemRepository

    private JpaAccountRepository accountRepository

    @Override
    Item create(ItemCreationBody body, Long lenderId) {
        Account lender = accountRepository.findById(lenderId)
                .orElseThrow { new ItemLenderDoesNotExistException("The account $lenderId does not exist.") }
        Item item = new ItemBuilderImpl()
                .price(body.price)
                .rentDaysDuration(body.rentingDays)
                .description(body.description)
                .assuranceCost(body.assuranceCost)
                .lender(lender)
                .title(body.title)
                .build()
        itemRepository.save(item)
        item
    }

    @Override
    List<Item> listAll() {
        itemRepository.findAll()
                .stream()
                .filter { !it.isBeingUsed() }
                .collect(Collectors.toList())
    }

    @Override
    Item republish(ItemRepublishingBody body, Long itemId) {
        def item = itemRepository.findById(itemId)
                .orElseThrow { new ItemDoesNotExistException("The item $itemId does not exist.") }
        item.republish(body.price, ofDays(body.rentingDays))
        return item
    }
}
