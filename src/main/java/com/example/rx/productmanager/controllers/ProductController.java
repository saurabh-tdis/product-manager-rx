package com.example.rx.productmanager.controllers;

import com.example.rx.productmanager.domain.Product;
import com.example.rx.productmanager.domain.ProductPrice;
import com.example.rx.productmanager.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ProductController {

    final ProductService productService;

    @GetMapping("/products")
    Flux<Product> findAll(){
        return productService.findAll();
    }

    @GetMapping("/products/{id}")
    Mono<ResponseEntity<Product>> findById(@PathVariable("id") Long id){
        ResponseEntity<Product> notFound = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return productService
                .findById(id)
                .map(p-> new ResponseEntity<>(p, HttpStatus.OK))
                .switchIfEmpty(Mono.just(notFound));
    }

    @PostMapping("/products")
    Mono<ResponseEntity<Product>> save(@Valid @RequestBody Product product){
        ResponseEntity<Product> badRequest = new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        return productService.create(product)
                    .map(p-> new ResponseEntity<Product>(p, HttpStatus.OK));
                    //.onErrorReturn(new ResponseEntity<Product>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/products/{id}")
    Mono<ResponseEntity<Product>> update(@PathVariable("id") Long id, @RequestBody Product product){
        return productService.update(product)
                .map(p-> new ResponseEntity<Product>(p, HttpStatus.OK))
                .onErrorReturn(new ResponseEntity<Product>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/products")
    Mono<Void> delete(@PathVariable("id") Long id){
        return productService.delete(id);
    }

    @GetMapping(value = "/product-prices", produces = "application/stream+json")
    Flux<ProductPrice> getProductPrices(@RequestParam ("product") String product){
        return productService.getProductPrices(product);
    }

}
