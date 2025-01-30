package com.benjamin.cqrs.domain.entities;

import com.benjamin.cqrs.domain.entities.valueobjects.Content;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
public class Review {
    private Double score;
    private Product product;
    private Content content;
    private List<ReviewComment> comments;
    private List<ReviewReaction> reactions;

    public void addReaction(ReviewReaction reaction) {
        this.reactions.add(reaction);
    }

    public void addComment(ReviewComment comment) {
        this.comments.add(comment);
    }
}
