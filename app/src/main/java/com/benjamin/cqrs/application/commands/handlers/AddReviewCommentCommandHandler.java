package com.benjamin.cqrs.application.commands.handlers;

import com.benjamin.cqrs.application.ReviewUseCases;
import com.benjamin.cqrs.application.commands.AddReviewCommentCommand;
import com.benjamin.cqrs.application.commands.CommandHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddReviewCommentCommandHandler implements CommandHandler<AddReviewCommentCommand> {

    private final ReviewUseCases review;

    @Override
    public void onCommand(AddReviewCommentCommand command) {
        this.review.addReviewComment(command);
    }
}
