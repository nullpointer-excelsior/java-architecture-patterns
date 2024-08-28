package com.benjamin.hexagonal.infrastructure.adapters;

import com.benjamin.hexagonal.domain.entities.Professional;
import com.benjamin.hexagonal.domain.ports.ProfessionalRepository;
import com.benjamin.hexagonal.infrastructure.database.JpaProfessionalRepository;
import com.benjamin.hexagonal.infrastructure.database.ProfessionalEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.stream.Stream;

@Repository
@AllArgsConstructor
public class PostgresProfessionalRepository implements ProfessionalRepository {

    private final JpaProfessionalRepository repository;

    @Override
    public void save(Professional p) {
        var entity = ProfessionalEntity.builder()
                .id(UUID.fromString(p.getId()))
                .firstname(p.getFirstname())
                .lastname(p.getLastname())
                .build();
        repository.save(entity);
    }

    @Override
    public void update(Professional p) {
        this.save(p);
    }

    @Override
    public Stream<Professional> findAll() {
        return repository.findAll()
                .stream()
                .map(e ->Professional.builder()
                        .id(e.getId().toString())
                        .firstname(e.getFirstname())
                        .lastname(e.getLastname())
                .build());
    }
}
