package com.benjamin.cqrs.application.queries;

public record GetReviewsByProductQuery(String sku) implements Query {}
