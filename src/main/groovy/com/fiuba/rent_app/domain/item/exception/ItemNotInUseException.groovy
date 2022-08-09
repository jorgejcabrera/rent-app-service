package com.fiuba.rent_app.domain.item.exception


class ItemNotInUseException extends RuntimeException {
    ItemNotInUseException(String message) {
        super(message)
    }
}
