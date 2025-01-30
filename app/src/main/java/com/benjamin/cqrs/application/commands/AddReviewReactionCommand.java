package com.benjamin.cqrs.application.commands;

import com.benjamin.cqrs.domain.entities.ReactionType;

public record AddReviewReactionCommand(String reviewId, String userId, ReactionType reaction) {}
