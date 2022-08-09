package com.fiuba.rent_app.domain.item.exception

class InvalidLenderIdException extends RuntimeException {
    InvalidLenderIdException(String message) {
        super(message)
    }
}
