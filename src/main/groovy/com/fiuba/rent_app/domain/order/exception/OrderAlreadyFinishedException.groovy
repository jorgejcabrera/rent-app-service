package com.fiuba.rent_app.domain.order.exception

class OrderAlreadyFinishedException extends RuntimeException {

    OrderAlreadyFinishedException(String message) {
        super(message)
    }
}

