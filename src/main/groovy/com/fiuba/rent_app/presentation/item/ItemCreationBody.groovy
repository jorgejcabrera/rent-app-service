package com.fiuba.rent_app.presentation.item

import com.fasterxml.jackson.annotation.JsonProperty

class ItemCreationBody {
    @JsonProperty("description")
    public String description

    @JsonProperty("price")
    public BigDecimal price

    @JsonProperty("assurance_cost")
    public BigDecimal assuranceCost

    @JsonProperty("renting_days")
    public Integer rentingDays

    @JsonProperty("title")
    public String title

    ItemCreationBody() {}
}


