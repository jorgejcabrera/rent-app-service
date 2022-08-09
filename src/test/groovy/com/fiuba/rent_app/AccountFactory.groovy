package com.fiuba.rent_app

import com.fiuba.rent_app.domain.account.Account

class AccountFactory {

    private static borrowerId = 2
    private static lenderId = 1
    private static debtorId = 3
    static Account BORROWER = of(borrowerId)
    static Account LENDER = of(lenderId)
    static Account DEBTOR = new Account(id: debtorId, email: "cabrerajjorge@gmail.com", items: [TestItemFactory.withExpiredOrderBy(debtorId)])

    static Account of(Long accountId) {
        return new Account(id: accountId, email: "jocabrera@fi.uba.com.ar")
    }
}
