package com.benjamin.cqrs.application.commands.handlers;

import com.benjamin.cqrs.application.ReviewCommentUseCases;
import com.benjamin.cqrs.application.commands.AddReviewCommentReactionCommand;
import com.benjamin.cqrs.application.commands.CommandHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddReviewCommentReactionCommandHandler implements CommandHandler<AddReviewCommentReactionCommand> {

    private final ReviewCommentUseCases reviewCommentUseCases;

    @Override
    public void onCommand(AddReviewCommentReactionCommand command) {
        reviewCommentUseCases.addReviewCommentReaction(command);
    }
}
