package com.fiuba.rent_app.domain.order.rule

class InvalidRenterException extends RuntimeException {
    InvalidRenterException(String message) {
        super(message)
    }
}
