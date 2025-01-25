package com.benjamin.eventsourcing.domain.ports.repository;

import com.benjamin.eventsourcing.domain.projections.OrderProjection;

public interface OrderProjectionRepository {
    void save(OrderProjection order);
}
