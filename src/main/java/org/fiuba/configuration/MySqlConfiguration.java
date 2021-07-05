package org.fiuba.configuration;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Primary
@Configuration
public class MySqlConfiguration extends DataSourceProperties {

    public void afterPropertiesSet() throws Exception {
        this.setUsername("user");
        this.setPassword("admin");
        super.afterPropertiesSet();
    }
}