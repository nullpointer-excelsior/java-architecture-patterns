package com.benjamin.hexagonal.application;

import com.benjamin.hexagonal.application.dto.CreateProfessionalRequest;
import com.benjamin.hexagonal.application.dto.CreateProfessionalResponse;
import com.benjamin.hexagonal.domain.entities.Professional;
import com.benjamin.hexagonal.domain.events.ProfessionalCreatedEvent;
import com.benjamin.hexagonal.domain.ports.DomainEventBus;
import com.benjamin.hexagonal.domain.ports.ProfessionalRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProfessionalUseCases {

    private final ProfessionalRepository repository;
    private final DomainEventBus eventBus;

    public CreateProfessionalResponse createProfessional(CreateProfessionalRequest request) {
        var professional = Professional.create(request.firstname(), request.lastname());
        this.repository.save(professional);
        this.eventBus.publish(new ProfessionalCreatedEvent(professional.getId()));
        return new CreateProfessionalResponse(professional.getId(), professional.getFirstname(), professional.getLastname());
    }

}
