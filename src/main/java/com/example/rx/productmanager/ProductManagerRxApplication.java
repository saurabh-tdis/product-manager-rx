package com.example.rx.productmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class ProductManagerRxApplication {
    static {
        //BlockHound.install();
    }
    public static void main(String[] args) {
        SpringApplication.run(ProductManagerRxApplication.class, args);
    }


}
