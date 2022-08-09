package com.fiuba.rent_app.presentation.item


import com.fiuba.rent_app.domain.item.Item
import com.fiuba.rent_app.domain.item.service.ItemService
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponse
import com.fiuba.rent_app.presentation.item.response.ItemHttpResponseFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PutMapping(value = "/v1/item/{item_id}/publishing")
    ResponseEntity<ItemHttpResponse> republish(
            @PathVariable("item_id") Long itemId,
            @RequestBody ItemRepublishingBody body
    ) {
        Item item = itemService.update(body, itemId)
        return responseFactory.from(item)
    }

    @PutMapping(value = "/v1/item/{item_id}/returning")
    ResponseEntity returning(
            @PathVariable("item_id") Long itemId,
            @RequestHeader("x-caller-id") Long userId
    ) {
        itemService.free(itemId, userId)
        return ResponseEntity.ok().build()
    }
}