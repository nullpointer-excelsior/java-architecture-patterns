package com.benjamin.hexagonal.infrastructure.adapters;

import com.benjamin.hexagonal.domain.events.DomainEvent;
import com.benjamin.hexagonal.domain.ports.DomainEventBus;

public class InMemoryDomainEventBus implements DomainEventBus {
    @Override
    public void publish(DomainEvent<?> event) {

    }
}
