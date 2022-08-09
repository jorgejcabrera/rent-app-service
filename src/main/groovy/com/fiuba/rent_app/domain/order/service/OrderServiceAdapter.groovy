package com.fiuba.rent_app.domain.order.service

import com.fiuba.rent_app.datasource.account.JpaAccountRepository
import com.fiuba.rent_app.datasource.item.JpaItemRepository
import com.fiuba.rent_app.datasource.order.JpaOrderRepository
import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.order.Order
import com.fiuba.rent_app.domain.order.exception.InvalidCallerException
import com.fiuba.rent_app.domain.order.exception.ItemIsNotAvailableForOrderingException
import com.fiuba.rent_app.domain.order.exception.ItemNotFoundException
import org.springframework.transaction.annotation.Transactional

class OrderServiceAdapter implements OrderService {

    private JpaItemRepository itemRepository
    private JpaOrderRepository orderRepository
    private JpaAccountRepository accountRepository

    @Override
    @Transactional
    Order createFor(Long itemId, Long borrowerId) {
        Account borrower = accountRepository.findById(borrowerId)
                .orElseThrow { throw new InvalidCallerException("The borrower $borrowerId does not exist") }
        Item item = itemRepository.findById(itemId)
                .orElseThrow { new ItemNotFoundException("Item $itemId does not exist") }
        if (!item.canBeRented()) {
            throw new ItemIsNotAvailableForOrderingException("The item ${item.getId()} is not avilable.")
        }
        Order order = new Order(item, borrower)
        borrower.addOrder(order)
        accountRepository.save(borrower)
        orderRepository.save(order)
        order
    }

}