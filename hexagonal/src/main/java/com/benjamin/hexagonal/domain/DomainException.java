package com.benjamin.hexagonal.domain;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
