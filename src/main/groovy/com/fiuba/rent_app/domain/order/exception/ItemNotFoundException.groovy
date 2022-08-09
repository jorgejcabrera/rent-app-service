package com.fiuba.rent_app.domain.order.exception

class ItemNotFoundException extends RuntimeException {

    ItemNotFoundException(String message) {
        super(message)
    }
}
