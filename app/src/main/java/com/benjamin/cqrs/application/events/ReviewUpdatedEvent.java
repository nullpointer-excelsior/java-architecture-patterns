package com.benjamin.cqrs.application.events;

import com.benjamin.cqrs.domain.entities.Review;

import java.time.LocalDateTime;

public record ReviewUpdatedEvent(String eventId, LocalDateTime createdAt, Review payload) implements Event{

}
