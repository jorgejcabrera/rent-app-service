package com.fiuba.rent_app.presentation

import com.fiuba.rent_app.domain.item.Item
import org.springframework.http.ResponseEntity

interface ItemHttpResponseFactory {
    ResponseEntity<ItemHttpResponse> from(Item item)
}