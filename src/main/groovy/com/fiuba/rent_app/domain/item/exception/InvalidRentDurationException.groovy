package com.fiuba.rent_app.domain.item.exception

class InvalidRentDurationException extends RuntimeException {
    InvalidRentDurationException(String message) {
        super(message)
    }
}
