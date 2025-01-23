package com.benjamin.eventsourcing.domain.events;

import com.benjamin.eventsourcing.domain.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class OrderCompletedEvent extends Event {
    private String orderId;
    private OrderStatus status;
}
