package com.fiuba.rent_app.domain.item.service

class ItemLenderDoesNotExistException extends RuntimeException {
    ItemLenderDoesNotExistException(String message) {
        super(message)
    }
}
