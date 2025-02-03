package com.benjamin.cqrs.infrastructure.adapters.integration;

import com.benjamin.cqrs.application.commands.Command;
import com.benjamin.cqrs.domain.entities.ReactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.benjamin.cqrs.application.commands.*;
import com.benjamin.cqrs.application.commands.handlers.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConcurrentCommandBusTest {

    @Mock
    private AddReviewCommandHandler addReviewCommandHandler;

    @Mock
    private AddReviewCommentCommandHandler addReviewCommentCommandHandler;

    @Mock
    private AddReviewReactionCommandHandler addReviewReactionCommandHandler;

    @Mock
    private AddReviewCommentReactionCommandHandler addReviewCommentReactionCommandHandler;

    @InjectMocks
    private ConcurrentCommandBus concurrentCommandBus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("GIVEN AddReviewCommand WHEN dispatch THEN handle the command")
    void givenAddReviewCommand_whenDispatch_thenHandleCommand() {
        // Arrange
        AddReviewCommand command = new AddReviewCommand("1234", 7.0, "user-id", "good");
        // Act
        concurrentCommandBus.dispatch(command);
        // Assert
        verify(addReviewCommandHandler, times(1)).onCommand(command);
        verifyNoMoreInteractions(addReviewCommentCommandHandler);
    }

    @Test
    @DisplayName("GIVEN AddReviewCommentCommand WHEN dispatch THEN handle the command")
    void givenAddReviewCommentCommand_whenDispatch_thenHandleCommand() {
        // Arrange
        AddReviewCommentCommand command = new AddReviewCommentCommand("1234", "user-id", "Good!");
        // Act
        concurrentCommandBus.dispatch(command);
        // Assert
        verify(addReviewCommentCommandHandler, times(1)).onCommand(command);
        verifyNoMoreInteractions(addReviewCommentCommandHandler);
    }

    @Test
    @DisplayName("GIVEN AddReviewReactionCommand WHEN dispatch THEN handle the command")
    void givenAddReviewReactionCommand_whenDispatch_thenHandleCommand() {
        // Arrange
        AddReviewReactionCommand command = new AddReviewReactionCommand("1234", "1234", ReactionType.HELPFUL);
        // Act
        concurrentCommandBus.dispatch(command);
        // Assert
        verify(addReviewReactionCommandHandler, times(1)).onCommand(command);
        verifyNoMoreInteractions(addReviewReactionCommandHandler);
    }

    @Test
    @DisplayName("GIVEN AddReviewCommentReactionCommand WHEN dispatch THEN handle the command")
    void givenAddReviewCommentReactionCommand_whenDispatch_thenHandleCommand() {
        // Arrange
        AddReviewCommentReactionCommand command = new AddReviewCommentReactionCommand("1234", "1234", ReactionType.DISLIKE);
        // Act
        concurrentCommandBus.dispatch(command);
        // Assert
        verify(addReviewCommentReactionCommandHandler, times(1)).onCommand(command);
        verifyNoMoreInteractions(addReviewCommentReactionCommandHandler);
    }

    @Test
    @DisplayName("GIVEN Unknown Command WHEN dispatch THEN throw IllegalArgumentException")
    void givenUnknownCommand_whenDispatch_thenThrowIllegalArgumentException() {
        // Arrange
        Command unknownCommand = new Command() {};
        // Act & Assert
        assertThatThrownBy(() -> concurrentCommandBus.dispatch(unknownCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown command");
    }
}
