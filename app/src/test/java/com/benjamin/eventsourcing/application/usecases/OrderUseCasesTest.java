package com.benjamin.eventsourcing.application.usecases;

import com.benjamin.eventsourcing.application.commands.CompleteOrderCommand;
import com.benjamin.eventsourcing.application.commands.CreateOrderCommand;
import com.benjamin.eventsourcing.application.commands.UpdateOrderToDeliveredCommand;
import com.benjamin.eventsourcing.domain.entities.OrderStatus;
import com.benjamin.eventsourcing.domain.entities.Product;
import com.benjamin.eventsourcing.domain.events.Event;
import com.benjamin.eventsourcing.domain.events.OrderCompletedEvent;
import com.benjamin.eventsourcing.domain.events.OrderCreatedEvent;
import com.benjamin.eventsourcing.domain.events.OrderDeliveredEvent;
import com.benjamin.eventsourcing.domain.ports.repository.OrderEventStore;
import com.benjamin.eventsourcing.domain.ports.repository.OrderProjectionRepository;
import com.benjamin.eventsourcing.domain.projections.OrderProjection;
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
    @Mock
    private OrderProjectionRepository orderProjectionRepository;
    private OrderUseCases orderUseCases;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderUseCases = new OrderUseCases(orderEventStore, orderProjectionRepository);
    }

    @Test
    @DisplayName("GIVEN OrderUseCases instance WHEN createOrder THEN should save OrderCreatedEvent")
    void createOrder_shouldSaveOrderCreatedEvent() {
        // Arrange
        String orderId = "order-123";
        List<Product> products = List.of(
                new Product("product-1", "guitar", 2),
                new Product("product-2", "bass", 3)
        );
        CreateOrderCommand command = new CreateOrderCommand(orderId, products);
        // Act
        orderUseCases.createOrder(command);
        // Asserts
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(orderEventStore, times(1)).save(eventCaptor.capture());

        assertThat(eventCaptor.getValue())
                .isInstanceOf(OrderCreatedEvent.class)
                .extracting("orderId", "products", "total", "status")
                .containsExactly(orderId, products, 5, OrderStatus.CREATED);

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
        when(orderEventStore.findByOrderId(orderId)).thenReturn(Stream.of(orderCreatedEvent));
        // Act
        orderUseCases.updateOrderToDelivered(command);
        // Assert
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(orderEventStore, times(1))
                .save(eventCaptor.capture());

        assertThat(eventCaptor.getValue())
                .isInstanceOf(OrderDeliveredEvent.class)
                .extracting(
                        "orderId",
                        "shipping.id",
                        "shipping.address",
                        "shipping.option",
                        "status"
                )
                .containsExactly(
                        orderId,
                        shipping.id(),
                        shipping.address(),
                        shipping.option(),
                        OrderStatus.DELIVERED
                );

    }

    @Test
    @DisplayName("GIVEN OrderUseCases instance WHEN completeOrder THEN should save OrderCompletedEvent")
    void completeOrder_shouldSaveOrderCompletedEvent() {
        // Arrange
        String orderId = "order-123";
        OrderCreatedEvent createdEvent = new OrderCreatedEvent(
                orderId,
                List.of(new Product("product-1", "guitar", 2)),
                2,
                OrderStatus.CREATED
        );
        OrderDeliveredEvent deliveredEvent = new OrderDeliveredEvent(
                orderId,
                null,
                OrderStatus.DELIVERED
        );
        when(orderEventStore.findByOrderId(orderId)).thenReturn(Stream.of(createdEvent, deliveredEvent));
        // Act
        orderUseCases.completeOrder(new CompleteOrderCommand(orderId));
        // Assert
        ArgumentCaptor<OrderProjection> orderProjectionCaptor = ArgumentCaptor.forClass(OrderProjection.class);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(orderEventStore, times(1)).save(eventCaptor.capture());
        verify(orderProjectionRepository, times(1)).save(orderProjectionCaptor.capture());

        assertThat(eventCaptor.getValue())
                .isInstanceOf(OrderCompletedEvent.class)
                .extracting("orderId", "status")
                .containsExactly(orderId, OrderStatus.COMPLETED);
        var orderProjectionValue = orderProjectionCaptor.getValue();
        assertThat(orderProjectionValue)
                .extracting("id", "total")
                .containsExactly(orderId, 2);
        assertThat(orderProjectionValue.products()).hasSize(1);

    }

    @Test
    @DisplayName("GIVEN OrderUseCases instance WHEN completeOrder on non-delivered order THEN should throw exception")
    void completeOrder_nonDeliveredOrder_shouldThrowException() {
        // Arrange
        String orderId = "order-123";
        OrderCreatedEvent createdEvent = new OrderCreatedEvent(
                orderId,
                List.of(new Product("product-1", "guitar", 2)),
                2,
                OrderStatus.CREATED
        );
        when(orderEventStore.findByOrderId(orderId)).thenReturn(Stream.of(createdEvent));
        // Act & Assert
        assertThatThrownBy(() -> orderUseCases.completeOrder(new CompleteOrderCommand(orderId)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Order cannot be completed because it is not in DELIVERED state");
    }


}
