package com.benjamin.cqrs.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class User {
    private String id;
    private String fullname;
    private String email;
}
