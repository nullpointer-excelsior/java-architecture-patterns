package com.benjamin.cqrs.domain.entities.valueobjects;

import com.benjamin.cqrs.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Content {
    private User user;
    private String content;
}
