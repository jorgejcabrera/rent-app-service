package com.fiuba.rent_app.domain.order.rule

class ItemIsNotAvailableForOrderingException extends RuntimeException {

    ItemIsNotAvailableForOrderingException(String message) {
        super(message)
    }
}
