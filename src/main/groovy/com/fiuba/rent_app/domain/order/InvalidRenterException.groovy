package com.fiuba.rent_app.domain.order

class InvalidRenterException extends RuntimeException {
    InvalidRenterException(String message) {
        super(message)
    }
}
