package com.benjamin.eventsourcing.domain.ports.repository;

import com.benjamin.eventsourcing.domain.entities.Order;

import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    void update(Order order);
    Optional<Order> findById(String id);
}
