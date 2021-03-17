package com.example.rx.productmanager.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
@Slf4j
public class ProductManagerConfig {

    @Bean
    ConnectionFactoryInitializer connectionFactoryInitializer(ConnectionFactory connectionFactory){
        ConnectionFactoryInitializer initialiazer = new ConnectionFactoryInitializer();
        initialiazer.setConnectionFactory(connectionFactory);
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        //populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
        initialiazer.setDatabasePopulator(populator);
        log.info("database initialized");
        return initialiazer;
    }
}
