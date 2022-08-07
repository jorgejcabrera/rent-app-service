package com.fiuba.rent_app.domain.item.service

import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item

import com.fiuba.rent_app.domain.order.service.ItemNotFoundException
import com.fiuba.rent_app.presentation.item.ItemCreationBody
import com.fiuba.rent_app.presentation.item.ItemRepublishingBody

import java.util.stream.Collectors

import static java.time.Duration.*
import static java.time.LocalDateTime.now

class ItemServiceImpl implements ItemService {

    private JpaItemRepository itemRepository

    private JpaAccountRepository accountRepository

    @Override
    Item create(ItemCreationBody body, Long lenderId) {
        Account lender = accountRepository.findById(lenderId)
                .orElseThrow { new ItemLenderDoesNotExistException("The account $lenderId does not exist.") }
        Item item = new Item(
                account: lender,
                description: body.description,
                price: body.price,
                title: body.title,
                rentDay: now(),
                rentDuration: ofDays(body.rentingDays),
                assuranceCost: body.assuranceCost)
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
                .orElseThrow { new ItemNotFoundException("The item $itemId does not exist.") }
        item.republish(body.price, ofDays(body.rentingDays))
        return item
    }
}
