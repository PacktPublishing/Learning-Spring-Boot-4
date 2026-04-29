package com.example.ai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductTools {

    private static final Map<String, Double> PRICES = Map.of(
            "spring-book",   49.99,
            "java-guide",    39.99,
            "docker-manual", 29.99
    );

    @Tool(description = "Returns the current price of a product given its SKU identifier.")
    public String getProductPrice(String sku) {
        Double price = PRICES.get(sku.toLowerCase());
        return price == null ? "Product not found" : "Price: $" + price;
    }
}
