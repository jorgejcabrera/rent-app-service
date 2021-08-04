package com.fiuba.rent_app.domain.item.builder

import com.fiuba.rent_app.domain.account.Account
import com.fiuba.rent_app.domain.item.Item


interface ItemBuilder {

    ItemBuilder id(Long id)

    ItemBuilder description(String description)

    ItemBuilder title(String title)

    ItemBuilder price(BigDecimal price)

    ItemBuilder lender(Account renter)

    ItemBuilder rentDaysDuration(int daysDuration)

    Item build()

}