package com.benjamin.eventsourcing.domain.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@RequiredArgsConstructor
public abstract class Event {
    private final String id;
    private final LocalDateTime createdAt;

    public Event() {
        this(UUID.randomUUID().toString(), LocalDateTime.now());
    }
}
