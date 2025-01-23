package com.benjamin.eventsourcing.domain;

import com.benjamin.eventsourcing.domain.dtos.CreateOrderDto;
import com.benjamin.eventsourcing.domain.entities.Order;
import com.benjamin.eventsourcing.domain.entities.OrderStatus;
import com.benjamin.eventsourcing.domain.entities.Product;
import com.benjamin.eventsourcing.domain.entities.Shipping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class OrderTest {

    @Test
    @DisplayName("GIVEN Order class WHEN invoke Order.create() THEN Order should be valid")
    void createOrderTest() {
        var dto = new CreateOrderDto("abc123", List.of(
                new Product("1111", "guitar", 1),
                new Product("2222", "bas", 1)
        ));
        var order = Order.create(dto);

        assertThat(order.getId()).isEqualTo(dto.orderId());
        assertThat(order.getProducts()).hasSize(2);
        assertThat(order.getEvents()).hasSize(1);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    @DisplayName("GIVEN Order instance WHEN delivered() THEN Order should has a shipping")
    void deliveredOrderTest() {
        var dto = new CreateOrderDto("abc123", List.of(
                new Product("1111", "guitar", 1),
                new Product("2222", "bas", 1)
        ));
        var order = Order.create(dto);
        order.delivered(new Shipping("abc456", "los alamos 123", "santiago"));

        assertThat(order.getShipping()).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    @DisplayName("GIVEN Order instance WHEN complete() THEN Order should state COMPLETED")
    void completeOrderTest() {
        var dto = new CreateOrderDto("abc123", List.of(
                new Product("1111", "guitar", 1),
                new Product("2222", "bas", 1)
        ));
        var order = Order.create(dto);
        order.delivered(new Shipping("abc456", "los alamos 123", "santiago"));
        order.complete();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("GIVEN Order instance WHEN complete() AND state is not equals DELIVERED THEN Order should raise IllegalStateException")
    void completeOrderErrorTest() {
        var dto = new CreateOrderDto("abc123", List.of(
                new Product("1111", "guitar", 1),
                new Product("2222", "bas", 1)
        ));
        var order = Order.create(dto);

        assertThatThrownBy(order::complete)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Order cannot be completed because it is not in DELIVERED state");
    }
}
