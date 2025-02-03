package com.benjamin.cqrs.application.queries;

import java.util.List;

public interface QueryHandler<Q extends Query, R> {
    List<R> onQuery(Q query);
}
