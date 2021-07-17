package com.fiuba.rent_app.presentation.item

import com.fasterxml.jackson.annotation.JsonProperty

class ItemCreationBody {
    @JsonProperty("description")
    public String description

    @JsonProperty("price")
    public BigDecimal price

    @JsonProperty("renting_days")
    public Integer rentingDays

    ItemCreationBody(String description, BigDecimal price, Integer rentingDays) {
        this.description = description
        this.price = price
        this.rentingDays = rentingDays
    }

    ItemCreationBody() {}
}


