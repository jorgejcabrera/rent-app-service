package com.fiuba.rent_app.domain.item

class InvalidLenderIdException extends RuntimeException {
    InvalidLenderIdException(String message) {
        super(message)
    }
}
