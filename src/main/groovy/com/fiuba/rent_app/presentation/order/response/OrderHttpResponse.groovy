package com.fiuba.rent_app.presentation.order.response

import org.springframework.web.bind.annotation.ResponseBody

import java.time.LocalDateTime

@ResponseBody
class OrderHttpResponse {
    Long id
    String lenderId
    LocalDateTime expiredRentDate
}
