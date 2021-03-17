package com.example.rx.productmanager.controllers;

import com.example.rx.productmanager.domain.Product;
import com.example.rx.productmanager.handlers.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductEndpoints {

    @Bean
    RouterFunction<ServerResponse> routes(ProductHandler handler){
        return route(GET("/products").and(accept(APPLICATION_JSON)), handler::findAll)
                .andRoute(GET("/products/{id}").and(accept(APPLICATION_JSON)), handler::findById);
    }

    @Bean
    RouterFunction<ServerResponse> nestedRoutes(ProductHandler handler){
        return nest(GET("/products"),
                nest(accept(APPLICATION_JSON),
                        route(GET("/"), handler::findAll))
                        .andRoute(GET("/{id}"), handler::findById));
    }
}
