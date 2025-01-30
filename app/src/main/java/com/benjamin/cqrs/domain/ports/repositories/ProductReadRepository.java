package com.benjamin.cqrs.domain.ports.repositories;

import com.benjamin.cqrs.domain.entities.Product;

import java.util.Optional;

public interface ProductReadRepository {
    Optional<Product> findBySku(String sku);
}
