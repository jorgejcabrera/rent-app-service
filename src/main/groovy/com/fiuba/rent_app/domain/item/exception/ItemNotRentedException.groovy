package com.fiuba.rent_app.domain.item.exception

class ItemNotRentedException extends RuntimeException {
    ItemNotRentedException(String message) {
        super(message)

    }
}
