package com.fiuba.rent_app.presentation.item

import com.fasterxml.jackson.annotation.JsonProperty

class ItemCreationBody {
    @JsonProperty("description")
    public String description;

    @JsonProperty("price")
    public BigDecimal price;

    @JsonProperty("rent_days_duration")
    public Integer rentDaysDuration;

    ItemCreationBody(String description, BigDecimal price, Integer rentDaysDuration) {
        this.description = description;
        this.price = price;
        this.rentDaysDuration = rentDaysDuration;
    }

    ItemCreationBody() {
    }
}


