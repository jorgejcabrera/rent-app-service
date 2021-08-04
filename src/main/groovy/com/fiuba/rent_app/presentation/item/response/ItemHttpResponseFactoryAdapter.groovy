package com.fiuba.rent_app.presentation.item.response

import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponse
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponseFactory
import org.springframework.http.ResponseEntity

import static org.springframework.http.HttpStatus.*

class ItemHttpResponseFactoryAdapter implements ItemHttpResponseFactory {
    @Override
    ResponseEntity<ItemHttpResponse> from(Item item) {
        if (item == null) {
            return ResponseEntity
                    .status(CONFLICT)
                    .body(null)
        }
        ItemHttpResponse response = asItemHttpResponse(item)
        return ResponseEntity
                .status(CREATED)
                .body(response)
    }

    @Override
    ResponseEntity<List<ItemHttpResponse>> from(List<Item> items) {
        List<ItemHttpResponse> itemHttpResponse = new ArrayList<ItemHttpResponse>()
        items.forEach { item ->
            ItemHttpResponse response = asItemHttpResponse(item)
            itemHttpResponse.add(response)
        }
        return ResponseEntity
                .status(OK)
                .body(itemHttpResponse)
    }

    private static ItemHttpResponse asItemHttpResponse(Item item) {
        ItemHttpResponse response = new ItemHttpResponse(
                id: item.id,
                description: item.description,
                title: item.title,
                price: item.price,
                rentDuration: item.rentDuration)
        response
    }


}
