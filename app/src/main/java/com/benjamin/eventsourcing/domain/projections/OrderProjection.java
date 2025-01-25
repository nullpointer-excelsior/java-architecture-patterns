package com.benjamin.eventsourcing.domain.projections;

import java.util.List;

public record OrderProjection(String id, List<ProductProjection> products, Integer total) { }
