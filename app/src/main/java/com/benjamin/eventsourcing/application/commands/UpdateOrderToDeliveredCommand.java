package com.benjamin.eventsourcing.application.commands;

public record UpdateOrderToDeliveredCommand(String orderId, Shipping shipping) {

    public record Shipping(String id, String address, String option) {

    }
}
