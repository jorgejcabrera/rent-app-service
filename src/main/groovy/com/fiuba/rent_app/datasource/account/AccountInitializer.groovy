package com.fiuba.rent_app.datasource.account

import com.fiuba.rent_app.domain.account.Account

class AccountInitializer {

     JpaAccountRepository accountRepository

    void run() {
        accountRepository.save(new Account(email: "cabrerajjorge@gmail.com"))
        accountRepository.save(new Account(email: "jocabrera@fi.uba.ar"))
    }
}
