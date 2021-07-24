package com.fiuba.rent_app.presentation

import com.fiuba.rent_app.domain.item.service.ItemBorrowerDoesNotExistException
import com.fiuba.rent_app.domain.order.builder.rule.InvalidRenterException
import com.fiuba.rent_app.domain.order.builder.rule.ItemIsNotAvailableForOrderingException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(ItemIsNotAvailableForOrderingException.class)
    ResponseEntity<ErrorResponse> handle(ItemIsNotAvailableForOrderingException exception) {
        return ResponseEntity
                .status(HttpStatus.PRECONDITION_FAILED)
                .body(new ErrorResponse(errorCode: "item_not_available", message: exception.message))
    }

    @ExceptionHandler(InvalidRenterException.class)
    ResponseEntity<ErrorResponse> handle(InvalidRenterException exception) {
        return ResponseEntity
                .status(HttpStatus.PRECONDITION_FAILED)
                .body(new ErrorResponse(errorCode: "invalid_renter", message: exception.message))
    }

    @ExceptionHandler(ItemBorrowerDoesNotExistException.class)
    ResponseEntity<ErrorResponse> handle(ItemBorrowerDoesNotExistException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(errorCode: "invalid_borrower", message: exception.message))
    }
}
