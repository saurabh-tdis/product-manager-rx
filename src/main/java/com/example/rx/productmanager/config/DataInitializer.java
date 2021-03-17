package com.example.rx.productmanager.config;

import com.example.rx.productmanager.domain.Product;
import com.example.rx.productmanager.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        productRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(List.of(
                        new Product("product-A", "product-A", 100.00),
                        new Product("product-B", "product-B", 200.00),
                        new Product("product-C", "product-C", 300.00),
                        new Product("product-D", "product-D", 400.00),
                        new Product("product-E", "product-E", 500.00))))
                .flatMap(p -> productRepository.save(p))
                .thenMany(productRepository.findAll())
                .subscribe(data-> {
                    log.info("" + data);
                    log.info("Data Initialized!!!");
                });
    }
}
