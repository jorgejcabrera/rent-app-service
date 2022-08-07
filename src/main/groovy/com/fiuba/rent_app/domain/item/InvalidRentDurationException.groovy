package com.fiuba.rent_app.domain.item

class InvalidRentDurationException extends RuntimeException {
    InvalidRentDurationException(String message) {
        super(message)
    }
}
