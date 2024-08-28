package com.benjamin.hexagonal.application;

import com.benjamin.hexagonal.application.dto.CreateProfessionalRequest;
import com.benjamin.hexagonal.domain.DomainException;
import com.benjamin.hexagonal.domain.events.ProfessionalCreatedEvent;
import com.benjamin.hexagonal.domain.ports.DomainEventBus;
import com.benjamin.hexagonal.domain.ports.ProfessionalRepository;
import com.benjamin.hexagonal.domain.utils.DomainUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class ProfessionalUseCasesTest {

    private ProfessionalUseCases useCases;
    private ProfessionalRepository repositoryMock;
    private DomainEventBus eventBusMock;


    @BeforeEach
    void setUp(){
        repositoryMock = Mockito.mock(ProfessionalRepository.class);
        eventBusMock = Mockito.mock(DomainEventBus.class);
        useCases = new ProfessionalUseCases(repositoryMock, eventBusMock);
    }

    @Test
    @DisplayName("GIVEN create professional use case WHEN invoke ProfessionalUseCases.create() THEN return CreateProfessionalResponse with id defined as UUID format")
    void createProfessionalTest(){

        Mockito.doNothing().when(repositoryMock).save(any());

        var request = new CreateProfessionalRequest("pepito", "los palotes");
        var result = useCases.createProfessional(request);

        verify(repositoryMock).save(any());
        Assertions.assertThat(result.firstname()).isEqualTo(request.firstname());
        Assertions.assertThat(result.lastname()).isEqualTo(request.lastname());
        Assertions.assertThat(result.id()).isNotEmpty();
        Assertions.assertThatCode(() -> UUID.fromString(result.id())).doesNotThrowAnyException();

    }

    @Test
    @DisplayName("GIVEN create professional use case WHEN invoke ProfessionalUseCases.create() THEN DomainEventBus.publish() invoked with professionalCreatedEvent")
    void createProfessionalWithEventsTest(){

        Mockito.doNothing().when(repositoryMock).save(any());
        Mockito.doNothing().when(eventBusMock).publish(any());

        var request = new CreateProfessionalRequest("pepito", "los palotes");
        var result = useCases.createProfessional(request);

        verify(repositoryMock).save(any());
        verify(eventBusMock, times(1)).publish(argThat(argument -> argument instanceof ProfessionalCreatedEvent));
        Assertions.assertThat(result.firstname()).isEqualTo(request.firstname());
        Assertions.assertThat(result.lastname()).isEqualTo(request.lastname());
        Assertions.assertThat(result.id()).isNotEmpty();


    }

    @Test
    @DisplayName("GIVEN create professional use case WHEN invoke ProfessionalUseCases.create() THEN validate CreateProfessionalRequest input")
    void createProfessionalRequestValidationTest(){

        Mockito.doNothing().when(repositoryMock).save(any());
        Mockito.doNothing().when(eventBusMock).publish(any());

        var invalidRequest = new CreateProfessionalRequest("", null);

        assertThatThrownBy(() -> useCases.createProfessional(invalidRequest))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("firstname")
                .hasMessageContaining("lastname");


    }
}
