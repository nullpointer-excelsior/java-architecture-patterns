package com.benjamin.cqrs.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ReviewReaction {
    private User user;
    private ReactionType type;
}
