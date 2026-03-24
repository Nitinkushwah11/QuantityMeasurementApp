package com.app.quantitymeasurement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * QuantityMeasurementApplication.java
 *
 * This is the entry point for the Spring Boot application. 
 * It enables auto-configuration, component scanning, and 
 * provides metadata for the OpenAPI/Swagger documentation.
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Quantity Measurement API",
        version = "1.0.0",
        description = "REST API for quantity measurements with support for multiple unit types"
    )
)
public class QuantityMeasurementAppApplication {

    public static void main(String[] args) {
        // Bootstrap the Spring application
        SpringApplication.run(QuantityMeasurementAppApplication.class, args);
    }
}