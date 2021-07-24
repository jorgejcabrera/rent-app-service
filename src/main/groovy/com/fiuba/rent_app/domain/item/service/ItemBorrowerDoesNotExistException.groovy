package com.fiuba.rent_app.domain.item.service

class ItemBorrowerDoesNotExistException extends RuntimeException {
    ItemBorrowerDoesNotExistException(String message) {
        super(message)
    }
}
