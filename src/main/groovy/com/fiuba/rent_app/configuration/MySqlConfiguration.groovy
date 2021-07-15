package com.fiuba.rent_app.configuration

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Primary
@Configuration
class MySqlConfiguration extends DataSourceProperties {

    void afterPropertiesSet() throws Exception {
        this.setUsername("user")
        this.setPassword("admin")
        super.afterPropertiesSet()
    }
}