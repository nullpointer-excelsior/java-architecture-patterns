package com.benjamin.cqrs.domain.ports.repositories;

import com.benjamin.cqrs.domain.entities.User;

import java.util.Optional;

public interface UserReadRepository {
    Optional<User> findById(String id);
}
