package com.benjamin.cqrs.application.commands;

public record AddReviewCommentCommand(String reviewId, String userId, String content) implements Command {}
