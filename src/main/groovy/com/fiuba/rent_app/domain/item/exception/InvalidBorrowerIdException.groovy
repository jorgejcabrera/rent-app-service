package com.fiuba.rent_app.domain.item.exception

class InvalidBorrowerIdException extends RuntimeException {
    InvalidBorrowerIdException(String message) {
        super(message)
    }
}
