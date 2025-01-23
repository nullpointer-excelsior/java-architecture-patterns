package com.benjamin.eventsourcing.domain.events;

import com.benjamin.eventsourcing.domain.entities.OrderStatus;
import com.benjamin.eventsourcing.domain.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class OrderCreatedEvent extends Event {
    private String orderId;
    private List<Product> products;
    private Integer total;
    private OrderStatus status;
}
