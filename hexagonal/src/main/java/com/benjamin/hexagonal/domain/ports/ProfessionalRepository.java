package com.benjamin.hexagonal.domain.ports;

import com.benjamin.hexagonal.domain.entities.Professional;

import java.util.stream.Stream;

public interface ProfessionalRepository {
     void save(Professional p);
     void update(Professional p);
     Stream<Professional> findAll();
}
