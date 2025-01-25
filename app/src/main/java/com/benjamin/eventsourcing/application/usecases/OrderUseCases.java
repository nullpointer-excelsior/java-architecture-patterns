package com.benjamin.eventsourcing.application.usecases;

import com.benjamin.eventsourcing.application.commands.CompleteOrderCommand;
import com.benjamin.eventsourcing.application.commands.CreateOrderCommand;
import com.benjamin.eventsourcing.application.commands.UpdateOrderToDeliveredCommand;
import com.benjamin.eventsourcing.domain.entities.Order;
import com.benjamin.eventsourcing.domain.entities.Shipping;
import com.benjamin.eventsourcing.domain.ports.repository.OrderEventStore;
import com.benjamin.eventsourcing.domain.ports.repository.OrderRepository;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class OrderUseCases {

    private OrderEventStore orderEventStore;
    private OrderRepository orderRepository;

    public void createOrder(CreateOrderCommand command) {
        var order = Order.create(
                command.orderId(),
                command.products()
        );
        order.getEvents()
                .forEach(event -> this.orderEventStore.save(event));
        order.cleanEvents();
    }

    public void updateOrderToDelivered(UpdateOrderToDeliveredCommand command) {
        var eventStream = this.orderEventStore.findByOrderId(command.orderId());
        var order = Order.fromEventStream(eventStream);
        order.delivered(new Shipping(
                command.shipping().id(),
                command.shipping().address(),
                command.shipping().option()
        ));
        order.getEvents()
                .forEach(event -> this.orderEventStore.save(event));
        order.cleanEvents();
    }

    public void completeOrder(CompleteOrderCommand command) {
        var eventStream = this.orderEventStore.findByOrderId(command.orderId());
        var order = Order.fromEventStream(eventStream);
        order.complete();
        order.getEvents()
                .forEach(event -> this.orderEventStore.save(event));
        this.orderRepository.save(order);
        order.cleanEvents();
    }
}
