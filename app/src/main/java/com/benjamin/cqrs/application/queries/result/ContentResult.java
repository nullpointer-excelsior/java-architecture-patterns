package com.benjamin.cqrs.application.queries.result;

import com.benjamin.cqrs.application.queries.Result;
import com.benjamin.cqrs.domain.entities.valueobjects.Content;

public record ContentResult(String user, String content) implements Result {
    public static ContentResult map(Content content) {
        return new ContentResult(content.getUser().getFullname(), content.getContent());
    }
}
