package com.fiuba.rent_app.presentation.item

import com.fasterxml.jackson.annotation.JsonProperty

class ItemRepublishingBody {
    @JsonProperty("price")
    public BigDecimal price

    @JsonProperty("renting_days")
    public Integer rentingDays
}
