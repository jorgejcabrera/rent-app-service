package com.fiuba.rent_app.presentation.item

import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.service.ItemService
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponse
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponseFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ItemController {

    @Autowired
    private ItemService itemService

    @Autowired
    private ItemHttpResponseFactory responseFactory

    @PostMapping(value = "/v1/user/{id}/item")
    ResponseEntity<ItemHttpResponse> create(
            @PathVariable("id") Long renterId,
            @RequestBody ItemCreationBody body) {
        Item newItem = itemService.create(body, renterId)
        return responseFactory.from(newItem)
    }

    @GetMapping(value = "/v1/items")
    ResponseEntity<List<ItemHttpResponse>> listAll() {
        List<Item> allItems = itemService.listAll()
        return responseFactory.from(allItems)
    }
}