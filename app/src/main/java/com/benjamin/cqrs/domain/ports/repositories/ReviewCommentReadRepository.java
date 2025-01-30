package com.benjamin.cqrs.domain.ports.repositories;

import com.benjamin.cqrs.domain.entities.ReviewComment;

import java.util.List;
import java.util.Optional;

public interface ReviewCommentReadRepository {
    Optional<ReviewComment> findById(String id);
    List<ReviewComment> findByReviewId(String id);
}
