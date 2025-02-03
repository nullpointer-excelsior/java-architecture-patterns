package com.benjamin.cqrs.application.queries.handlers;

import com.benjamin.cqrs.application.ReviewCommentUseCases;
import com.benjamin.cqrs.application.queries.GetReviewCommentsQuery;
import com.benjamin.cqrs.application.queries.QueryHandler;
import com.benjamin.cqrs.application.queries.result.ReviewCommentResult;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetReviewCommentsQueryHandler implements QueryHandler<GetReviewCommentsQuery, ReviewCommentResult> {

    private final ReviewCommentUseCases reviewCommentUseCases;

    @Override
    public List<ReviewCommentResult> onQuery(GetReviewCommentsQuery query) {
        return reviewCommentUseCases.getReviewComments(query);
    }
}
