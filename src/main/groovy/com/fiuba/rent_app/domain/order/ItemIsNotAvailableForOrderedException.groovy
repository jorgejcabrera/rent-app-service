package com.fiuba.rent_app.domain.order

class ItemIsNotAvailableForOrderedException extends RuntimeException {

    ItemIsNotAvailableForOrderedException(String message) {
        super(message)
    }
}
