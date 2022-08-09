package com.fiuba.rent_app.presentation

import com.fiuba.rent_app.domain.account.AccountWithDebtException
import com.fiuba.rent_app.domain.item.exception.EmptyItemPriceException
import com.fiuba.rent_app.domain.item.exception.EmptyItemTitleException
import com.fiuba.rent_app.domain.item.exception.InvalidRentDurationException
import com.fiuba.rent_app.domain.item.exception.ItemInUseException
import com.fiuba.rent_app.domain.item.exception.ItemLenderDoesNotExistException
import com.fiuba.rent_app.domain.order.exception.InvalidCallerException
import com.fiuba.rent_app.domain.order.exception.ItemIsNotAvailableForOrderingException
import com.fiuba.rent_app.domain.order.exception.ItemNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED

@ControllerAdvice
class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ItemInUseException.class)
    ResponseEntity<ErrorResponse> handle(ItemInUseException exception) {
        return ResponseEntity
                .status(PRECONDITION_FAILED)
                .body(new ErrorResponse(errorCode: "item_in_use", message: exception.message))
    }

    @ExceptionHandler(AccountWithDebtException.class)
    ResponseEntity<ErrorResponse> handle(AccountWithDebtException exception) {
        return ResponseEntity
                .status(PRECONDITION_FAILED)
                .body(new ErrorResponse(errorCode: "account_with_debt", message: exception.message))
    }

    @ExceptionHandler(InvalidRentDurationException.class)
    ResponseEntity<ErrorResponse> handle(InvalidRentDurationException exception) {
        return ResponseEntity
                .status(PRECONDITION_FAILED)
                .body(new ErrorResponse(errorCode: "invalid_rent_duration", message: exception.message))
    }

    @ExceptionHandler(EmptyItemPriceException.class)
    ResponseEntity<ErrorResponse> handle(EmptyItemPriceException exception) {
        return ResponseEntity
                .status(PRECONDITION_FAILED)
                .body(new ErrorResponse(errorCode: "item_price_empty", message: exception.message))
    }

    @ExceptionHandler(EmptyItemTitleException.class)
    ResponseEntity<ErrorResponse> handle(EmptyItemTitleException exception) {
        return ResponseEntity
                .status(PRECONDITION_FAILED)
                .body(new ErrorResponse(errorCode: "item_title_empty", message: exception.message))
    }

    @ExceptionHandler(ItemIsNotAvailableForOrderingException.class)
    ResponseEntity<ErrorResponse> handle(ItemIsNotAvailableForOrderingException exception) {
        return ResponseEntity
                .status(PRECONDITION_FAILED)
                .body(new ErrorResponse(errorCode: "item_not_available", message: exception.message))
    }

    @ExceptionHandler(InvalidCallerException.class)
    ResponseEntity<ErrorResponse> handle(InvalidCallerException exception) {
        return ResponseEntity
                .status(PRECONDITION_FAILED)
                .body(new ErrorResponse(errorCode: "invalid_caller", message: exception.message))
    }

    @ExceptionHandler(ItemLenderDoesNotExistException.class)
    ResponseEntity<ErrorResponse> handle(ItemLenderDoesNotExistException exception) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ErrorResponse(errorCode: "invalid_borrower", message: exception.message))
    }

    @ExceptionHandler(ItemNotFoundException.class)
    ResponseEntity<ErrorResponse> handle(ItemNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ErrorResponse(errorCode: "invalid_item", message: exception.message))
    }
}
