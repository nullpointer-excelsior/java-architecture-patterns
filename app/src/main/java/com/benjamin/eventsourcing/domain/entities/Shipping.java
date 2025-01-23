package com.benjamin.eventsourcing.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Shipping {
    private String id;
    private String address;
    private String option;
}
