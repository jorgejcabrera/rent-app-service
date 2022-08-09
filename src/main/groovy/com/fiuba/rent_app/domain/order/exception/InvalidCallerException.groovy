package com.fiuba.rent_app.domain.order.exception

class InvalidCallerException extends RuntimeException {
    InvalidCallerException(String message) {
        super(message)
    }
}
