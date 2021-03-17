package com.example.rx.productmanager.domain;

import lombok.Data;

@Data
public class ProductQuote {

    //@JsonProperty("shop")
    String shop;

    String product;

    //@JsonProperty("price")
    Double price;

    //@JsonProperty("discountCode")
    String discountCode;

    String currency;

}
