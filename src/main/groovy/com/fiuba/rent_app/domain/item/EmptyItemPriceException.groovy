package com.fiuba.rent_app.domain.item

class EmptyItemPriceException extends RuntimeException {
    EmptyItemPriceException(String message) {
        super(message)
    }
}
