package com.benjamin.eventsourcing.domain.ports.integration;

import com.benjamin.eventsourcing.domain.events.Event;

public interface EventBus {
    void dispatch(Event event);
}
