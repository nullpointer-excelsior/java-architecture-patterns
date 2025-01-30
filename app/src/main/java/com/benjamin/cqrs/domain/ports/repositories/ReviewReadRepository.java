package com.benjamin.cqrs.domain.ports.repositories;

import com.benjamin.cqrs.domain.entities.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewReadRepository {
    Optional<Review> findById(String id);
    List<Review> findByProductSku(String sku);
}
