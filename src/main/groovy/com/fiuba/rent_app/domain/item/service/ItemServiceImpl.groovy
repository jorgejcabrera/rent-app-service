package com.fiuba.rent_app.domain.item.service

import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.exception.ItemLenderDoesNotExistException
import com.fiuba.rent_app.domain.order.exception.ItemNotFoundException
import com.fiuba.rent_app.presentation.item.ItemCreationBody
import com.fiuba.rent_app.presentation.item.ItemRepublishingBody

import java.util.stream.Collectors

import static java.time.Duration.ofDays

class ItemServiceImpl implements ItemService {

    private JpaItemRepository itemRepository

    private JpaAccountRepository accountRepository

    @Override
    Item create(ItemCreationBody body, Long lenderId) {
        Account lender = accountRepository.findById(lenderId)
                .orElseThrow { new ItemLenderDoesNotExistException("The account $lenderId does not exist.") }
        Item item = new Item(lender, body.description, body.price, body.title, body.rentingDays, body.assuranceCost)
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
    Item update(ItemRepublishingBody body, Long itemId) {
        def item = itemRepository.findById(itemId)
                .orElseThrow { new ItemNotFoundException("The item $itemId does not exist.") }
        item.update(body.price, ofDays(body.rentingDays))
        return item
    }

    @Override
    Item free(Long itemId, Long borrowerId) {
        def item = itemRepository.findById(itemId)
                .orElseThrow { new ItemNotFoundException("The item $itemId does not exist.") }
        item.free(borrowerId)
        itemRepository.save(item)
        item
    }
}
