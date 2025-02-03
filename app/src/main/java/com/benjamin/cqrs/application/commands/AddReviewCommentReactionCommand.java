package com.benjamin.cqrs.application.commands;

import com.benjamin.cqrs.domain.entities.ReactionType;

public record AddReviewCommentReactionCommand(String reviewCommentId, String userId, ReactionType reaction) implements Command {}
