package com.benjamin.cqrs.application.ports.integration;

import com.benjamin.cqrs.application.queries.Query;
import com.benjamin.cqrs.application.queries.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface QueryBus {
    <R extends Result> CompletableFuture<List<R>> query(Query query, Class<R> queryType);
}
