package org.fiuba.presentation;

import org.fiuba.domain.Item;
import org.fiuba.domain.ItemBuilder;
import org.fiuba.domain.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping(value = "/v1/user/{id}/item")
    public ResponseEntity<Item> create(
            @PathVariable("id") Long renterId,
            @RequestBody ItemCreationBody body) {
        Item newItem = itemService.create(body, renterId);
        /*;*/
        return ResponseEntity
                .status(CREATED)
                .body(newItem);

    }
}

