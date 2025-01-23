package com.benjamin.eventsourcing.domain.dtos;

import com.benjamin.eventsourcing.domain.entities.Product;

import java.util.List;

public record CreateOrderDto(String orderId, List<Product> products) {
}
