package com.benjamin.cqrs.infrastructure.adapters.integration;

import com.benjamin.cqrs.application.ports.integration.QueryBus;
import com.benjamin.cqrs.application.queries.GetReviewCommentsQuery;
import com.benjamin.cqrs.application.queries.GetReviewsByProductQuery;
import com.benjamin.cqrs.application.queries.Query;
import com.benjamin.cqrs.application.queries.Result;
import com.benjamin.cqrs.application.queries.handlers.GetReviewCommentsQueryHandler;
import com.benjamin.cqrs.application.queries.handlers.GetReviewsByProductQueryHandler;
import lombok.Builder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Builder
public class ConcurrentQueryBus implements QueryBus {

    private final GetReviewCommentsQueryHandler getReviewCommentsQueryHandler;
    private final GetReviewsByProductQueryHandler getReviewsByProductQueryHandler;

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Result> CompletableFuture<List<R>> query(Query query, Class<R> queryType) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            return switch (query) {
                case GetReviewCommentsQuery getReviewCommentsQuery -> CompletableFuture.supplyAsync(
                        () -> (List<R>) getReviewCommentsQueryHandler.onQuery(getReviewCommentsQuery), executor);
                case GetReviewsByProductQuery getReviewsByProductQuery -> CompletableFuture.supplyAsync(
                        () -> (List<R>) getReviewsByProductQueryHandler.onQuery(getReviewsByProductQuery), executor);
                default -> throw new IllegalArgumentException("Unknown query:" + query.getClass().getSimpleName());
            };
        }
    }
}
