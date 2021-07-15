package com.fiuba.rent_app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(exclude = [DataSourceAutoConfiguration.class])
@EnableConfigurationProperties
@EnableJpaRepositories
@EntityScan
class RentappApplication {

    static void main(String[] args) {
        SpringApplication.run(RentappApplication, args)
    }

}
