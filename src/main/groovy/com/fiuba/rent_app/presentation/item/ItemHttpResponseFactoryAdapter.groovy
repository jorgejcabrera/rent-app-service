package com.fiuba.rent_app.presentation.item

import com.fiuba.rent_app.domain.item.Item
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import static org.springframework.http.HttpStatus.CREATED

class ItemHttpResponseFactoryAdapter implements ItemHttpResponseFactory {
    @Override
    ResponseEntity<ItemHttpResponse> from(Item item) {
        if (item == null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(null)
        }
        ItemHttpResponse response = new ItemHttpResponse(
                id: item.id,
                description: item.description,
                price: item.price,
                rentDuration: item.rentDuration)
        return ResponseEntity
                .status(CREATED)
                .body(response)
    }
}
