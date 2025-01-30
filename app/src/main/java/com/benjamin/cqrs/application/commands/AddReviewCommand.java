package com.benjamin.cqrs.application.commands;

import lombok.Builder;

@Builder
public record AddReviewCommand (String sku, Double score, String userId, String content) implements Command {}
