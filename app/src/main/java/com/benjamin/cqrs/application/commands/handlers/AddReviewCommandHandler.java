package com.benjamin.cqrs.application.commands.handlers;

import com.benjamin.cqrs.application.ReviewUseCases;
import com.benjamin.cqrs.application.commands.AddReviewCommand;
import com.benjamin.cqrs.application.commands.CommandHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddReviewCommandHandler implements CommandHandler<AddReviewCommand> {

    private final ReviewUseCases review;

    @Override
    public void onCommand(AddReviewCommand command) {
        this.review.addReview(command);
    }
}
