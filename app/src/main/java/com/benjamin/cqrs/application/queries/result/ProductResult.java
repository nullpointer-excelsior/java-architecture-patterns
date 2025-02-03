package com.benjamin.cqrs.application.queries.result;

import com.benjamin.cqrs.application.queries.Result;
import com.benjamin.cqrs.domain.entities.Product;

public record ProductResult(String sku, String name) implements Result {
    public static ProductResult map(Product product) {
        return new ProductResult(product.getSku(), product.getName());
    }
}
