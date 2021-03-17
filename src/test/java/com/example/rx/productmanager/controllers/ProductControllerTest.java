package com.example.rx.productmanager.controllers;

import com.example.rx.productmanager.repository.ProductRepository;
import com.example.rx.productmanager.services.ProductService;
import com.example.rx.productmanager.util.ProductBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    private ProductService productService;


    @BeforeEach
    public void setupEachTest(){
        Mockito
                .when(productService.findAll())
                .thenReturn(Flux.fromIterable(ProductBuilder.createProducts()));

        Mockito
                .when(productService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(ProductBuilder.createProducts().get(0)));
    }

    @Test
    public void testGetListOfProducts() {
        StepVerifier.create(productController.findAll())
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testFindProductById() {


        StepVerifier.create(productController.findById(1l))
                .expectNextMatches(p-> p.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    public void testFindProductByIdError() {
        Mockito
                .when(productService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(productController.findById(1l))
                .expectNextMatches(p-> p.getStatusCode()==HttpStatus.NOT_FOUND)
                .verifyComplete();
    }
}