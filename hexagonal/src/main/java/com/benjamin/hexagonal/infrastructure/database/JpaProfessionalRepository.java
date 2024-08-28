package com.benjamin.hexagonal.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaProfessionalRepository extends JpaRepository<ProfessionalEntity, UUID> {
}
