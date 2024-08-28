package com.benjamin.hexagonal.domain.entities;

import com.benjamin.hexagonal.domain.DomainException;
import com.benjamin.hexagonal.domain.utils.DomainUtils;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.util.stream.Collectors;

@Data
@Builder
public class Professional {

    @UUID
    @NotBlank
    private String id;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;

    public static Professional create(String firstname, String lastname) {
        var uuid = DomainUtils.generateUUID();
        var professional = Professional.builder()
                .id(uuid)
                .firstname(firstname)
                .lastname(lastname)
                .build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(professional);
            if (!violations.isEmpty()) {
                var message = violations
                        .stream()
                        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                        .collect(Collectors.joining(", "));
                throw new DomainException(message);
            }
        }
        return professional;
    }
}
