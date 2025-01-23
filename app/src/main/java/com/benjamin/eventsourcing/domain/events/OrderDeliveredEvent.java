package com.benjamin.eventsourcing.domain.events;

import com.benjamin.eventsourcing.domain.entities.OrderStatus;
import com.benjamin.eventsourcing.domain.entities.Shipping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter()
@ToString
@AllArgsConstructor()
public class OrderDeliveredEvent extends Event {
    private String orderId;
    private Shipping shipping;
    private OrderStatus status;
}
