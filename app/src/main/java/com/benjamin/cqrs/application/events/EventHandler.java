package com.benjamin.cqrs.application.events;

public interface EventHandler<T extends Event> {
    void onEvent(T event);
}
