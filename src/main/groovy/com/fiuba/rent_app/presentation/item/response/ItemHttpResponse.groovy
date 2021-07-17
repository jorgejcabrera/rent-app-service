package com.fiuba.rent_app.presentation.item.response

import org.springframework.web.bind.annotation.ResponseBody

import java.time.Duration

@ResponseBody
class ItemHttpResponse {
    Long id
    String description
    BigDecimal price
    Duration rentDuration
}
