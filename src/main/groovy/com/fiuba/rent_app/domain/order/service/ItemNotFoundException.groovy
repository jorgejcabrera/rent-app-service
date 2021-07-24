package com.fiuba.rent_app.domain.order.service

class ItemNotFoundException extends RuntimeException {

    ItemNotFoundException(String message) {
        super(message)
    }
}
