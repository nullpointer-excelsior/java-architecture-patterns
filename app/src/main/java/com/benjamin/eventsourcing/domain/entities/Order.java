package com.benjamin.eventsourcing.domain.entities;

import com.benjamin.eventsourcing.domain.events.Event;
import com.benjamin.eventsourcing.domain.events.OrderCompletedEvent;
import com.benjamin.eventsourcing.domain.events.OrderCreatedEvent;
import com.benjamin.eventsourcing.domain.events.OrderDeliveredEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {
    private String id;
    private List<Product> products;
    private Integer total;
    private OrderStatus status;
    private Shipping shipping;
    private List<Event> events = new ArrayList<>();

    public static Order create(String orderId, List<Product> products) {
        var order = new Order();
        var total = products.stream()
                .map(Product::getQuantity)
                .reduce(0, Integer::sum);
        var event = new OrderCreatedEvent(orderId, products, total, OrderStatus.CREATED);
        order.apply(event);
        order.events.add(event);
        return order;
    }

    public static Order fromEventStream(Stream<Event> events) {
        var order = new Order();
        events.forEach(event -> {
            switch (event) {
                case OrderCreatedEvent orderCreatedEvent -> order.apply(orderCreatedEvent);
                case OrderDeliveredEvent orderDeliveredEvent -> order.apply(orderDeliveredEvent);
                case OrderCompletedEvent orderCompletedEvent -> order.apply(orderCompletedEvent);
                default -> throw new IllegalStateException("Invalid event found: " + event.getClass().getName());
            }
        });
        return order;
    }

    public void delivered(Shipping shipping) {
        var event = new OrderDeliveredEvent(this.getId(), shipping, OrderStatus.DELIVERED);
        this.apply(event);
        this.events.add(event);
    }

    public void complete() {
        if (!this.status.equals(OrderStatus.DELIVERED)) {
            throw new IllegalStateException("Order cannot be completed because it is not in DELIVERED state");
        }
        var event = new OrderCompletedEvent(this.getId(), OrderStatus.COMPLETED);
        this.apply(event);
        this.events.add(event);
    }

    private void apply(OrderCreatedEvent event) {
        this.id = event.getOrderId();
        this.products = event.getProducts();
        this.status = event.getStatus();
        this.total = event.getTotal();
    }

    private void apply(OrderDeliveredEvent event) {
        this.shipping = event.getShipping();
        this.status = event.getStatus();
    }

    private void apply(OrderCompletedEvent event) {
        this.status = event.getStatus();
    }

    public void cleanEvents() {
        this.events = new ArrayList<>();
    }

}
