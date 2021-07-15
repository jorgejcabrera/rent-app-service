package com.fiuba.rent_app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
@EntityScan
class RentappApplication {

    static void main(String[] args) {
        SpringApplication.run(RentappApplication, args)
    }

}
