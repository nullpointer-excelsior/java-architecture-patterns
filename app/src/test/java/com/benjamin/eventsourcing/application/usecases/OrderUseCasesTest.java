package com.benjamin.eventsourcing.application.usecases;

import com.benjamin.eventsourcing.application.commands.CreateOrderCommand;
import com.benjamin.eventsourcing.application.commands.UpdateOrderToDeliveredCommand;
import com.benjamin.eventsourcing.domain.entities.Order;
import com.benjamin.eventsourcing.domain.entities.OrderStatus;
import com.benjamin.eventsourcing.domain.entities.Product;
import com.benjamin.eventsourcing.domain.entities.Shipping;
import com.benjamin.eventsourcing.domain.events.Event;
import com.benjamin.eventsourcing.domain.events.OrderCreatedEvent;
import com.benjamin.eventsourcing.domain.events.OrderDeliveredEvent;
import com.benjamin.eventsourcing.domain.ports.repository.OrderEventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderUseCasesTest {

    @Mock
    private OrderEventStore orderEventStore;
    private OrderUseCases orderUseCases;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderUseCases = new OrderUseCases(orderEventStore);
    }

    @Test
    @DisplayName("GIVEN OrderUseCases instance WHEN createOrder THEN should save OrderCreatedEvent")
    void createOrder_shouldSaveOrderCreatedEvent() {
        String orderId = "order-123";
        List<Product> products = List.of(
                new Product("product-1", "guitar", 2),
                new Product("product-2", "bass", 3)
        );
        CreateOrderCommand command = new CreateOrderCommand(orderId, products);

        orderUseCases.createOrder(command);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(orderEventStore, times(1)).save(eventCaptor.capture());

        Event capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent).isInstanceOf(OrderCreatedEvent.class);
        OrderCreatedEvent createdEvent = (OrderCreatedEvent) capturedEvent;
        assertThat(createdEvent.getOrderId()).isEqualTo(orderId);
        assertThat(createdEvent.getProducts()).isEqualTo(products);
        assertThat(createdEvent.getTotal()).isEqualTo(5);
        assertThat(createdEvent.getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    @DisplayName("GIVEN OrderUseCases instance WHEN updateOrderToDelivered THEN should save OrderDeliveredEvent")
    void updateOrderToDelivered_shouldSaveOrderDeliveredEvent() {
        // Arrange
        String orderId = "order-123";
        var shipping = new UpdateOrderToDeliveredCommand.Shipping("shipping-1", "123 Main St", "free");
        UpdateOrderToDeliveredCommand command = new UpdateOrderToDeliveredCommand(orderId, shipping);

        // Simulate existing events in the event stream
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                orderId,
                List.of(new Product("product-1", "guitar", 2)),
                2,
                OrderStatus.CREATED
        );

        when(orderEventStore.findByOrderId(orderId))
                .thenReturn(Stream.of(orderCreatedEvent));

        // Act
        orderUseCases.updateOrderToDelivered(command);

        // Assert
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(orderEventStore, times(1)).save(eventCaptor.capture());

        Event capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent).isInstanceOf(OrderDeliveredEvent.class);

        OrderDeliveredEvent deliveredEvent = (OrderDeliveredEvent) capturedEvent;
        assertThat(deliveredEvent.getOrderId()).isEqualTo(orderId);
        assertThat(deliveredEvent.getShipping().getId()).isEqualTo(shipping.id());
        assertThat(deliveredEvent.getShipping().getAddress()).isEqualTo(shipping.address());
        assertThat(deliveredEvent.getShipping().getOption()).isEqualTo(shipping.option());
        assertThat(deliveredEvent.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }


}
