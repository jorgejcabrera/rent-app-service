package com.fiuba.rent_app.presentation.order


import com.fiuba.rent_app.domain.order.Order
import com.fiuba.rent_app.domain.order.OrderService
import com.fiuba.rent_app.presentation.order.response.OrderHttpResponse
import com.fiuba.rent_app.presentation.order.response.OrderHttpResponseFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController {

    @Autowired
    private OrderService orderService

    @Autowired
    private OrderHttpResponseFactory responseFactory

    @PutMapping(value = "/v1/item/{id}/renting")
    ResponseEntity<OrderHttpResponse> rent(
            @PathVariable("id") String itemId,
            @RequestHeader("x-caller-id") Long callerId
    ) {
        Order order = orderService.createFor(itemId, callerId)
        return responseFactory.from(order)
    }
}

