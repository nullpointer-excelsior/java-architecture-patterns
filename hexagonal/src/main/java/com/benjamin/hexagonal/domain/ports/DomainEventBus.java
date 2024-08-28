package com.benjamin.hexagonal.domain.ports;

import com.benjamin.hexagonal.domain.events.DomainEvent;

public interface DomainEventBus {
    void publish(DomainEvent<?> event);
}
