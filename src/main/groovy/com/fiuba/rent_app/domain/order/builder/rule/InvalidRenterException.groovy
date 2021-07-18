package com.fiuba.rent_app.domain.order.builder.rule

class InvalidRenterException extends RuntimeException {
    InvalidRenterException(String message) {
        super(message)
    }
}
