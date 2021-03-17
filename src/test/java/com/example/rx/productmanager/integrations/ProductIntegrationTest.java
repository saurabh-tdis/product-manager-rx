package com.example.rx.productmanager.integrations;

import com.example.rx.productmanager.domain.Product;
import com.example.rx.productmanager.repository.ProductRepository;
import com.example.rx.productmanager.services.ProductService;
import com.example.rx.productmanager.util.ProductBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(ProductService.class)
public class ProductIntegrationTest {

    @MockBean
    ProductRepository productRepository;

    @Autowired
    WebTestClient testClient;

    @BeforeEach
    public void setUp(){
        Mockito.when(productRepository.findAll())
                .thenReturn(Flux.fromIterable(ProductBuilder.createProducts()));
    }

    @Test
    public void testFindAllProducts(){
        testClient
                .get()
                .uri("/products")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(ProductBuilder.createProducts().get(0).getId());
    }

    @Test
    public void testFindAllProductsWithoutJSON(){
        testClient
                .get()
                .uri("/products")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Product.class)
                .hasSize(4);

    }

}
