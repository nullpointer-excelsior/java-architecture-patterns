package com.benjamin.cqrs.domain.ports.repositories;

import com.benjamin.cqrs.domain.entities.Review;

public interface ReviewWriteRepository {
    void save(Review review);
}
