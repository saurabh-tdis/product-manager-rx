package com.example.rx.productmanager.domain;

import lombok.Data;

@Data
public class ProductPrice {

    String shop;

    Double originalPrice;

    Double finalPrice;
}
