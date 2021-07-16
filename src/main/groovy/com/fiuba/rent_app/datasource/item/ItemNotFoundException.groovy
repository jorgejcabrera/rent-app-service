package com.fiuba.rent_app.datasource.item

class ItemNotFoundException extends RuntimeException {

    ItemNotFoundException(String message) {
        super(message)
    }
}
