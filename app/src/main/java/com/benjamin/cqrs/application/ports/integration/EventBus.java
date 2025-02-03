package com.benjamin.cqrs.application.ports.integration;

import com.benjamin.cqrs.application.events.Event;

public interface EventBus {
    void dispatch(Event event);
}
