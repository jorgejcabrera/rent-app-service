package com.fiuba.rent_app.datasource.order

import com.fiuba.rent_app.domain.order.Order
import org.springframework.data.repository.CrudRepository

interface JpaOrderRepository extends CrudRepository<Order, Long> {
    Order save(Order order)
}
