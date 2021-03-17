package com.example.rx.productmanager.util;

import com.example.rx.productmanager.domain.Product;

import java.util.List;

public class ProductBuilder {

    public static Product createNewProduct(){
        return new Product("Product-ZZZ", "Product-ZZZ", 100.00);
    }

    public static Product createProductWithoutName(){
        return new Product("", "Product-ZZZ", 100.00);
    }

    public static List<Product> createProducts(){
        return List.of (
                        new Product(1l,"Product-AAA", "Product-AAA", 100.00),
                        new Product(2l,"Product-BBB","Product-BBB",200.00 ),
                        new Product(3l,"Product-CCC","Product-CCC",300.00 ),
                        new Product(4l,"Product-DDD","Product-DDD",400.00 ));
    }



}
