package com.benjamin.cqrs.application.queries.result;

import com.benjamin.cqrs.application.queries.Result;
import com.benjamin.cqrs.domain.entities.ReactionType;
import com.benjamin.cqrs.domain.entities.ReviewReaction;

public record ReviewReactionResult(String user, ReactionType reaction) implements Result {
    public static ReviewReactionResult map(ReviewReaction reaction) {
        return new ReviewReactionResult(reaction.getUser().getFullname(), reaction.getType());
    }
}
