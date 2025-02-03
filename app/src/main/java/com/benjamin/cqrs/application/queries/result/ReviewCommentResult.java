package com.benjamin.cqrs.application.queries.result;

import com.benjamin.cqrs.application.queries.Result;
import com.benjamin.cqrs.domain.entities.ReviewComment;

import java.util.List;

public record ReviewCommentResult(String id, ContentResult content, List<ReviewReactionResult> reactions) implements Result {
    public static ReviewCommentResult map(ReviewComment reviewComment){
        return new ReviewCommentResult(
                reviewComment.getId(),
                ContentResult.map(reviewComment.getContent()),
                reviewComment.getReactions().stream().map(ReviewReactionResult::map).toList());
    }
}
