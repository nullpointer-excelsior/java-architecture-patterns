package com.benjamin.cqrs.domain.ports.repositories;

import com.benjamin.cqrs.domain.entities.ReviewComment;

public interface ReviewCommentWriteRepository {
    void save(ReviewComment comment);
}
