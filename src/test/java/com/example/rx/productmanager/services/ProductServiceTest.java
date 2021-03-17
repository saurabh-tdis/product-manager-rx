package com.example.rx.productmanager.services;

import com.example.rx.productmanager.common.exceptions.EntityDuplicateException;
import com.example.rx.productmanager.repository.ProductRepository;
import com.example.rx.productmanager.util.ProductBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeAll
    public static void setup(){
        BlockHound.install();
    }

    @BeforeEach
    public void setupEachTest(){
        Mockito
                .when(productRepository.findAll())
                    .thenReturn(Flux.fromIterable(ProductBuilder.createProducts()));

        Mockito
                .when(productRepository.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Mono.just(ProductBuilder.createProducts().get(0)));

        Mockito
                .when(productRepository.save(ProductBuilder.createNewProduct()))
                .thenReturn(Mono.just(ProductBuilder.createProducts().get(0)));

        Mockito
                .when(productRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());
    }

    @Test
    public void blockHoundTest(){
        Mono.delay(Duration.ofSeconds(1))
                .doOnNext(it -> {
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .block();
    }

    @Test
    public void testGetListOfProducts() {
        StepVerifier.create(productService.findAll())
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testProductFindById() {
        StepVerifier.create(productService.findById(1l))
                .expectNextMatches(p-> p.getId()==1l)
                .verifyComplete();
    }

    @Test
    public void testProductSave(){
        StepVerifier.create(productService.create(ProductBuilder.createNewProduct()))
                .expectNextMatches(p-> p.getId()==1l)
                .verifyComplete();
    }

    @Test
    public void testProductDuplicateSave(){
        Mockito
                .when(productRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(ProductBuilder.createProducts().get(0)));

        StepVerifier.create(productService.create(ProductBuilder.createNewProduct()))
                .expectError(EntityDuplicateException.class)
                .verify();
    }


}
