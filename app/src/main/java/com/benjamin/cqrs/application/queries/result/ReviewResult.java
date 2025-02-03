package com.benjamin.cqrs.application.queries.result;

import com.benjamin.cqrs.application.queries.Result;
import com.benjamin.cqrs.domain.entities.Review;
import lombok.Builder;

@Builder
public record ReviewResult(Double score, ProductResult product, ContentResult content) implements Result {
    public static ReviewResult map(Review review) {
        return new ReviewResult(review.getScore(),
                ProductResult.map(review.getProduct()),
                ContentResult.map(review.getContent()));
    }
}
