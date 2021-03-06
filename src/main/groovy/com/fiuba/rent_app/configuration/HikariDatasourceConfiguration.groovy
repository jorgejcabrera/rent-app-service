package com.fiuba.rent_app.configuration;

import com.fiuba.rent_app.datasource.account.AccountInitializer;
import com.fiuba.rent_app.datasource.account.JpaAccountRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@Import(MySqlConfiguration.class)
class HikariDatasourceConfiguration {

    @Value("\${spring.datasource.config.lifetime}")
    private Integer maxLifetime = 0;

    @Value("\${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("\${spring.datasource.config.minimumIdle}")
    private Integer minimumIdle;

    @Value("\${spring.datasource.config.maximumPoolSize}")
    private Integer maximumPoolSize;

    @Value("\${spring.datasource.config.idleTimeout}")
    private Long idleTimeout;

    @Value("\${spring.datasource.config.connectionTimeout}")
    private Long connectionTimeout;

    @Value("\${spring.datasource.config.leakDetectionThreshold}")
    private Long leakDetectionThreshold;

    @Bean
    DataSource dataSource(DataSourceProperties properties) {
        HikariConfig config = new HikariConfig()
        config.setDriverClassName("com.mysql.cj.jdbc.Driver")
        config.setJdbcUrl(dataSourceUrl)
        config.setUsername(properties.getUsername())
        config.setPassword(properties.getPassword())
        config.setConnectionTimeout(connectionTimeout)
        config.setMinimumIdle(minimumIdle)
        config.setMaximumPoolSize(maximumPoolSize)
        config.setMaxLifetime(maxLifetime)
        config.setIdleTimeout(idleTimeout)
        config.setLeakDetectionThreshold(leakDetectionThreshold)
        return new HikariDataSource(config)
    }

    @Bean(initMethod = "run")
    AccountInitializer accountInitializer(JpaAccountRepository accountRepository) {
        return new AccountInitializer(accountRepository: accountRepository)
    }
}
