package com.fiuba.rent_app.domain.order.builder.exception

class InvalidRenterException extends RuntimeException {
    InvalidRenterException(String message) {
        super(message)
    }
}
