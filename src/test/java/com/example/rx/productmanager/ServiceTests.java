package com.example.rx.productmanager;

import com.example.rx.productmanager.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class ServiceTests {

    List<Product> products = List.of(new Product("A", "A", 100.00),
            new Product("B", "B", 200.00),
            new Product("C", "C", 300.00),
            new Product("D", "D", 400.00));

    Flux<Product> fluxProducts = null;

    @BeforeEach
    void setUp(){
        fluxProducts = Flux.fromIterable(products);
    }

    @Test
    void testFluxWithMap(){
        fluxProducts
                .map(p-> updatePrice(p, 100.00))
                .log()
                .thenMany(Flux.fromIterable(products))
                .subscribe(p-> {
                    System.out.println(p);
                });

        /*StepVerifier.create(updatedFlux)
                .expectNextMatches(p-> p.getPrice()==100.00)
                .verifyComplete();*/
    }

    Mono<Product> updatePrice(Product product, Double price){
        System.out.println("price updated");
        product.setPrice(price);
        return Mono.just(product);
    }
}
