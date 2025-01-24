package com.benjamin.eventsourcing.application.commands;

import com.benjamin.eventsourcing.domain.entities.Product;

import java.util.List;

public record CreateOrderCommand(String orderId, List<Product> products) {
}
