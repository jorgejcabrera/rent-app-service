package com.fiuba.rent_app.domain.item.exception

class EmptyItemPriceException extends RuntimeException {
    EmptyItemPriceException(String message) {
        super(message)
    }
}
