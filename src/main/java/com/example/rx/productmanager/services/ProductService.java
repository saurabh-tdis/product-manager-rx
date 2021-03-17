package com.example.rx.productmanager.services;

import com.example.rx.productmanager.common.exceptions.EntityDuplicateException;
import com.example.rx.productmanager.common.exceptions.EntityNotFoundException;
import com.example.rx.productmanager.domain.Product;
import com.example.rx.productmanager.domain.ProductPrice;
import com.example.rx.productmanager.domain.ProductQuote;
import com.example.rx.productmanager.domain.Shop;
import com.example.rx.productmanager.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    final ProductRepository productRepository;

    public Flux<Product> findAll(){
        return productRepository.findAll();
    }

    public Mono<Product> findById(Long id){
        return productRepository.findById(id);
    }

    public Mono<Product> findByName(String name){
        return productRepository.findByName(name);
    }

    public Mono<Product> create(Product product) {
        return productRepository
                .findByName(product.getName())
                .defaultIfEmpty(product)
                .flatMap(p-> {
                    if(p.getId()==null)
                        return productRepository.save(p);
                    else
                        return Mono.error(new EntityDuplicateException("Product name:" + product.getName() + " already exists."));
                });
    }

    public Mono<Product> update(Product product) {
        return productRepository
                    .findById(product.getId())
                    .switchIfEmpty(Mono.error(new EntityNotFoundException()))
                    .flatMap(p-> productRepository.save(p));
    }

    public Mono<Void> delete(Long id) {
        return productRepository.deleteById(id);
    }

    public Flux<ProductPrice> getProductPrices(String product){
        return getShops()
                    .flatMap(s-> getQuote(product, s.getName()))
                    .flatMap(q-> getPrice(q))
                    .flatMap(p-> getExchangeRate().zipWith(Mono.just(p)))
                    .map(t-> {
                        t.getT2().setFinalPrice(t.getT2().getFinalPrice()*t.getT1());
                        return t.getT2();
                    });
    }

    public Flux<Shop> getShops(){
        WebClient client = WebClient
                .create("http://localhost:8081/shops");
        return client
                .get()
                .retrieve()
                .bodyToFlux(Shop.class);
    }

    public Mono<ProductQuote> getQuote(String product, String shop){
        WebClient client = WebClient
                .builder().baseUrl("http://localhost:8081/quotes")
                .build();

        return client
                .get()
                .uri(req -> req.queryParam("shop", shop).queryParam("product", product).build())
                .retrieve()
                .bodyToMono(ProductQuote.class);
    }

    public Mono<ProductPrice> getPrice(ProductQuote quote){
        WebClient client = WebClient
                .builder().baseUrl("http://localhost:8081/price")
                .build();

        return client
                .get()
                .uri(r -> r
                        .queryParam("shop", quote.getShop())
                        .queryParam("product", quote.getProduct())
                        .queryParam("price", quote.getPrice())
                        .queryParam("discountCode", quote.getDiscountCode())
                        .build()
                )
                .retrieve()
                .bodyToMono(ProductPrice.class);
    }

    public Mono<Double> getExchangeRate(){
        WebClient client = WebClient
                .builder().baseUrl("http://localhost:8081/exchange")
                .build();

        return client
                .get()
                .retrieve()
                .bodyToMono(Double.class);
    }
}
