package com.benjamin.eventsourcing.domain.ports.repository;

import com.benjamin.eventsourcing.domain.events.Event;

import java.util.stream.Stream;

public interface OrderEventStore {
    void save(Event event);
    Stream<Event> findByOrderId(String orderId);
}
