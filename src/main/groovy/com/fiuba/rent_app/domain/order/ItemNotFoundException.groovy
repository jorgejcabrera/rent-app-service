package com.fiuba.rent_app.domain.order

class ItemNotFoundException extends RuntimeException {

    ItemNotFoundException(String message) {
        super(message)
    }
}
