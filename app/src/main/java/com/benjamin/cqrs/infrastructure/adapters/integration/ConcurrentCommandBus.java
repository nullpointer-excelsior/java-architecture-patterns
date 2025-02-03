package com.benjamin.cqrs.infrastructure.adapters.integration;

import com.benjamin.cqrs.application.commands.*;
import com.benjamin.cqrs.application.commands.handlers.AddReviewCommandHandler;
import com.benjamin.cqrs.application.commands.handlers.AddReviewCommentCommandHandler;
import com.benjamin.cqrs.application.commands.handlers.AddReviewCommentReactionCommandHandler;
import com.benjamin.cqrs.application.commands.handlers.AddReviewReactionCommandHandler;
import com.benjamin.cqrs.application.ports.integration.CommandBus;
import lombok.Builder;

import java.util.concurrent.Executors;

@Builder
public class ConcurrentCommandBus implements CommandBus {

    private final AddReviewCommandHandler addReviewCommandHandler;
    private final AddReviewCommentCommandHandler addReviewCommentCommandHandler;
    private final AddReviewReactionCommandHandler addReviewReactionCommandHandler;
    private final AddReviewCommentReactionCommandHandler addReviewCommentReactionCommandHandler;

    @Override
    public void dispatch(Command command) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            switch (command) {
                case AddReviewCommand addReviewCommand -> executor.submit(
                        () -> addReviewCommandHandler.onCommand(addReviewCommand));
                case AddReviewCommentCommand addReviewCommentCommand -> executor.submit(
                        () -> addReviewCommentCommandHandler.onCommand(addReviewCommentCommand));
                case AddReviewReactionCommand addReviewReactionCommand -> executor.submit(
                        () -> addReviewReactionCommandHandler.onCommand(addReviewReactionCommand));
                case AddReviewCommentReactionCommand addReviewCommentReactionCommand -> executor.submit(
                        () -> addReviewCommentReactionCommandHandler.onCommand(addReviewCommentReactionCommand));
                default -> throw new IllegalArgumentException("Unknown command: " + command.getClass().getSimpleName());
            }
        }
    }
}
