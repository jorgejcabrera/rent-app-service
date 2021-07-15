package com.fiuba.rent_app.presentation.order.response


import com.fiuba.rent_app.domain.order.Order
import org.springframework.http.ResponseEntity

interface OrderHttpResponseFactory {
    ResponseEntity<OrderHttpResponse> from(Order order)
}