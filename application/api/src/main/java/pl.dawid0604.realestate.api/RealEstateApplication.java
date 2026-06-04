/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "pl.dawid0604.realestate")
class RealEstateApplication {

    public static void main(final String[] args) {
        SpringApplication.run(RealEstateApplication.class, args);
    }
}
