package com.fiuba.rent_app.domain.item


class EmptyItemTitleException extends RuntimeException {
    EmptyItemTitleException(String message) {
        super(message)
    }
}
