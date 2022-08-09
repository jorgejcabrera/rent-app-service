package com.fiuba.rent_app.domain.account

class AccountWithDebtException extends RuntimeException {
    AccountWithDebtException(String message) {
        super(message)
    }
}
