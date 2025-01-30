package com.benjamin.cqrs.domain.entities;

import com.benjamin.cqrs.domain.entities.valueobjects.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class ReviewComment {
    private String id;
    private Content content;
    private List<ReviewReaction> reactions;

    public void addReaction(ReviewReaction reaction) {
        this.reactions.add(reaction);
    }
}
