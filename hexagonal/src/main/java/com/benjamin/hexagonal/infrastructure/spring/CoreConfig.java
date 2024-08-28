package com.benjamin.hexagonal.infrastructure.spring;

import com.benjamin.hexagonal.application.ProfessionalUseCases;
import com.benjamin.hexagonal.domain.ports.DomainEventBus;
import com.benjamin.hexagonal.domain.ports.ProfessionalRepository;
import com.benjamin.hexagonal.infrastructure.adapters.InMemoryDomainEventBus;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class CoreConfig {

    ProfessionalRepository professionalRepository;

    @Bean
    public ProfessionalUseCases getProfessionalUseCase() {
        return new ProfessionalUseCases(professionalRepository, getDomainEventBus());
    }

    @Bean
    public DomainEventBus getDomainEventBus() {
        return new InMemoryDomainEventBus();
    }

}
