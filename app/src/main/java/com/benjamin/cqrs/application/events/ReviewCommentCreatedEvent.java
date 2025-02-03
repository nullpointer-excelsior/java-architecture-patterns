package com.benjamin.cqrs.application.events;

import com.benjamin.cqrs.domain.entities.ReviewComment;

import java.time.LocalDateTime;

public record ReviewCommentCreatedEvent(String eventId, LocalDateTime createdAt, ReviewComment payload) implements Event {

}
