package com.benjamin.hexagonal.domain.utils;

import java.util.UUID;

public class DomainUtils {

    private DomainUtils() {
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
