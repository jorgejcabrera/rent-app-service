package com.fiuba.rent_app

import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.order.Order

import java.time.LocalDateTime

import static com.fiuba.rent_app.domain.order.Order.OrderStatus.*

class AccountFactory {

    private static borrowerId = 2
    private static lenderId = 1
    private static debtorId = 3
    static Account BORROWER = of(borrowerId)
    static Account LENDER = of(lenderId)
    static Account DEBTOR = new Account(
            id: debtorId,
            email: "cabrerajjorge@gmail.com",
            orders: [new Order(
                    id: 1,
                    item: TestItemFactory.withExpiredOrderBy(debtorId),
                    rentDay: LocalDateTime.now().minusDays(10),
                    status: OPEN,
            )]
    )

    static Account of(Long accountId) {
        return new Account(id: accountId, email: "jocabrera@fi.uba.com.ar")
    }
}
