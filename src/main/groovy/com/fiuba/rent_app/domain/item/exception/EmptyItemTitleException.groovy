package com.fiuba.rent_app.domain.item.exception


class EmptyItemTitleException extends RuntimeException {
    EmptyItemTitleException(String message) {
        super(message)
    }
}
