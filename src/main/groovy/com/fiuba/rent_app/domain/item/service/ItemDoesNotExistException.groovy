package com.fiuba.rent_app.domain.item.service


class ItemDoesNotExistException extends RuntimeException {
    ItemDoesNotExistException(String message) {
        super(message)
    }
}
