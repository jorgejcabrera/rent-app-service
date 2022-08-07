package com.fiuba.rent_app.domain.item

class ItemNotRentedException extends RuntimeException {
    ItemNotRentedException(String message) {
        super(message)

    }
}
