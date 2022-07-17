package com.fiuba.rent_app.domain.order

class OrderAlreadyFinishedException extends RuntimeException {

    OrderAlreadyFinishedException(String message) {
        super(message)
    }
}

