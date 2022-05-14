package com.fiuba.rent_app.presentation.order.response

import com.fiuba.rent_app.domain.order.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class OrderHttpResponseFactoryAdapter implements OrderHttpResponseFactory {

    @Override
    ResponseEntity<OrderHttpResponse> from(Order order) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new OrderHttpResponse(
                        id: order.id,
                        lenderId: order.lender,
                        expiredRentDate: order.item.expireRentDay()
                ))
    }
}
