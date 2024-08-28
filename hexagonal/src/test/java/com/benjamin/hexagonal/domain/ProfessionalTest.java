package com.benjamin.hexagonal.domain;

import com.benjamin.hexagonal.domain.entities.Professional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfessionalTest {


    @Test
    @DisplayName("GIVEN valid firstname and lastname WHEN create professional THEN should create professional successfully")
    void createProfessional_ValidInput_ShouldCreateSuccessfully() {
        String firstname = "John";
        String lastname = "Doe";

        Professional professional = Professional.create(firstname, lastname);

        assertThat(professional).isNotNull();
        assertThat(professional.getFirstname()).isEqualTo(firstname);
        assertThat(professional.getLastname()).isEqualTo(lastname);
        assertThat(professional.getId()).isNotNull();
    }

    @Test
    @DisplayName("GIVEN blank firstname WHEN create professional THEN should throw DomainException")
    void createProfessional_BlankFirstname_ShouldThrowException() {
        String blankFirstname = "";
        String lastname = "Doe";

        assertThatThrownBy(() -> Professional.create(blankFirstname, lastname))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("firstname: must not be blank");
    }

    @Test
    @DisplayName("GIVEN blank lastname WHEN create professional THEN should throw DomainException")
    void createProfessional_BlankLastname_ShouldThrowException() {
        String firstname = "John";
        String blankLastname = "";

        assertThatThrownBy(() -> Professional.create(firstname, blankLastname))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("lastname: must not be blank");
    }

}
