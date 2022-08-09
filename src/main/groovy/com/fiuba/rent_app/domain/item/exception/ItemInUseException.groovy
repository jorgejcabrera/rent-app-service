package com.fiuba.rent_app.domain.item.exception


class ItemInUseException extends RuntimeException {
    ItemInUseException(String message) {
        super(message)

    }
}
