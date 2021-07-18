package com.fiuba.rent_app.domain.order.builder.rule

class ItemIsNotAvailableForOrderingException extends RuntimeException {

    ItemIsNotAvailableForOrderingException(String message) {
        super(message)
    }
}
