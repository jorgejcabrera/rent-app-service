package com.fiuba.rent_app.domain.item.exception

class ItemLenderDoesNotExistException extends RuntimeException {
    ItemLenderDoesNotExistException(String message) {
        super(message)
    }
}
