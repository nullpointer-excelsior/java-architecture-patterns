package com.benjamin.cqrs.application.queries.handlers;

import com.benjamin.cqrs.application.ReviewUseCases;
import com.benjamin.cqrs.application.queries.GetReviewsByProductQuery;
import com.benjamin.cqrs.application.queries.QueryHandler;
import com.benjamin.cqrs.application.queries.result.ReviewResult;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetReviewsByProductQueryHandler implements QueryHandler<GetReviewsByProductQuery, ReviewResult> {

    private final ReviewUseCases reviewUseCases;

    @Override
    public List<ReviewResult> onQuery(GetReviewsByProductQuery query) {
        return this.reviewUseCases.getReviewsByProduct(query);
    }
}
