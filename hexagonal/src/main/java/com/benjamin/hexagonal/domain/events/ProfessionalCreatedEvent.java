package com.benjamin.hexagonal.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ProfessionalCreatedEvent extends DomainEvent<ProfessionalCreatedEvent.Payload> {

    public ProfessionalCreatedEvent(String professionalId) {
        super(new Payload(professionalId));
    }

    @Data
    @AllArgsConstructor
    public static class Payload {
        private String professionalId;
    }

}
