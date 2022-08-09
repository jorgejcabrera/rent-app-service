package com.fiuba.rent_app.domain.order.exception

class ItemIsNotAvailableForOrderingException extends RuntimeException {

    ItemIsNotAvailableForOrderingException(String message) {
        super(message)
    }
}
