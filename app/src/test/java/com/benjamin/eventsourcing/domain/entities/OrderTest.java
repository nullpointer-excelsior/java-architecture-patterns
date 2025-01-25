package com.benjamin.eventsourcing.domain.entities;

import com.benjamin.eventsourcing.domain.events.Event;
import com.benjamin.eventsourcing.domain.events.OrderCompletedEvent;
import com.benjamin.eventsourcing.domain.events.OrderCreatedEvent;
import com.benjamin.eventsourcing.domain.events.OrderDeliveredEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class OrderTest {

    @Test
    @DisplayName("GIVEN Order class WHEN invoke Order.create() THEN Order should be valid")
    void createOrderTest() {
        var orderId = "abc123";
        var order = Order.create("abc123", List.of(
                new Product("1111", "guitar", 1),
                new Product("2222", "bas", 1)
        ));

        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getProducts()).hasSize(2);
        assertThat(order.getEvents()).hasSize(1);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    @DisplayName("GIVEN Order instance WHEN delivered() THEN Order should has a shipping")
    void deliveredOrderTest() {
        var order = Order.create("abc123", List.of(
                new Product("1111", "guitar", 1),
                new Product("2222", "bas", 1)
        ));
        order.delivered(new Shipping("abc456", "los alamos 123", "santiago"));

        assertThat(order.getShipping()).isNotNull();
        assertThat(order.getEvents()).hasSize(2);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    @DisplayName("GIVEN Order instance WHEN complete() THEN Order should state COMPLETED")
    void completeOrderTest() {
        var order = Order.create("abc123", List.of(
                new Product("1111", "guitar", 1),
                new Product("2222", "bas", 1)
        ));
        order.delivered(new Shipping("abc456", "los alamos 123", "santiago"));
        order.complete();
        assertThat(order.getEvents()).hasSize(3);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("GIVEN Order instance WHEN complete() AND state is not equals DELIVERED THEN Order should raise IllegalStateException")
    void completeOrderErrorTest() {
        var order = Order.create("abc123", List.of(
                new Product("1111", "guitar", 1),
                new Product("2222", "bas", 1)
        ));

        assertThatThrownBy(order::complete)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Order cannot be completed because it is not in DELIVERED state");
    }

    @Test
    @DisplayName("GIVEN Order instance has events WHEN cleanEvents invoke THEN Order should has 0 events")
    void cleanEventsTest() {
        var order = Order.create("abc123", List.of(
                new Product("1111", "guitar", 1),
                new Product("2222", "bas", 1)
        ));
        order.delivered(new Shipping("abc456", "los alamos 123", "santiago"));

        assertThat(order.getEvents()).hasSize(2);
        order.cleanEvents();
        assertThat(order.getEvents()).hasSize(0);
    }

    @Test
    @DisplayName("GIVEN a valid event stream WHEN fromEventStream THEN returns an Order instance with applied events")
    void fromEventStream_withValidEvents_shouldReturnOrderWithAppliedEvents() {
        // Arrange
        String orderId = "order-123";
        List<Product> products = List.of(
                new Product("product-1", "guitar", 2),
                new Product("product-2", "bass", 3)
        );
        var shipping = new Shipping("shipping-123", "123 Main St", "New York");
        Event orderCreatedEvent = new OrderCreatedEvent(orderId, products, 5, OrderStatus.CREATED);
        Event orderDeliveredEvent = new OrderDeliveredEvent(orderId, shipping, OrderStatus.DELIVERED);
        Event orderCompletedEvent = new OrderCompletedEvent(orderId, OrderStatus.COMPLETED);
        Stream<Event> events = Stream.of(orderCreatedEvent, orderDeliveredEvent, orderCompletedEvent);
        // Act
        Order order = Order.fromEventStream(events);
        // Assert
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getProducts()).isEqualTo(products);
        assertThat(order.getTotal()).isEqualTo(5);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(order.getShipping()).isEqualTo(shipping);

    }

    @Test
    @DisplayName("GIVEN an event stream with invalid event WHEN fromEventStream THEN throws IllegalStateException")
    void fromEventStream_withInvalidEvent_shouldThrowException() {
        // Arrange
        Event invalidEvent = new Event() {}; // Anonymous event instance
        Stream<Event> events = Stream.of(invalidEvent);
        // Act & Assert
        assertThatThrownBy(() -> Order.fromEventStream(events))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid event found");
    }

}
