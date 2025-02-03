package com.benjamin.cqrs.application.commands.handlers;

import com.benjamin.cqrs.application.ReviewUseCases;
import com.benjamin.cqrs.application.commands.AddReviewReactionCommand;
import com.benjamin.cqrs.application.commands.CommandHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddReviewReactionCommandHandler implements CommandHandler<AddReviewReactionCommand> {

    private final ReviewUseCases reviewUseCases;

    @Override
    public void onCommand(AddReviewReactionCommand command) {
        reviewUseCases.addReviewReaction(command);
    }
}
