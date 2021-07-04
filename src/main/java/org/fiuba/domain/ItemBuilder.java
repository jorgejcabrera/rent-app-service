package org.fiuba.domain;

import java.math.BigDecimal;

public interface ItemBuilder {

    ItemBuilder description(String description);

    ItemBuilder price(BigDecimal price);

    ItemBuilder renter(Long renter);

    ItemBuilder rentDaysDuration(int daysDuration);

    Item build();

}
