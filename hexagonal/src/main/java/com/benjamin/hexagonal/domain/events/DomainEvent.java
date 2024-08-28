package com.benjamin.hexagonal.domain.events;

import com.benjamin.hexagonal.domain.utils.DomainUtils;
import lombok.Data;

@Data
public abstract class DomainEvent<T> {
    private String id;
    private T payload;

    protected DomainEvent(T payload) {
        this.id = DomainUtils.generateUUID();
        this.payload = payload;
    }
}
