package com.fiuba.rent_app.domain.order

class ItemIsNotAvailableForOrderingException extends RuntimeException {

    ItemIsNotAvailableForOrderingException(String message) {
        super(message)
    }
}
