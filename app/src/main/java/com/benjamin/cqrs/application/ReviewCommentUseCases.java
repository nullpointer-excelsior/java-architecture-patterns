package com.benjamin.cqrs.application;

import com.benjamin.cqrs.application.commands.AddReviewCommentReactionCommand;
import com.benjamin.cqrs.application.events.ReviewCommentCreatedEvent;
import com.benjamin.cqrs.application.events.ReviewCreatedEvent;
import com.benjamin.cqrs.application.ports.integration.EventBus;
import com.benjamin.cqrs.application.queries.GetReviewCommentsQuery;
import com.benjamin.cqrs.application.queries.result.ReviewCommentResult;
import com.benjamin.cqrs.domain.entities.ReviewReaction;
import com.benjamin.cqrs.domain.ports.repositories.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
public class ReviewCommentUseCases {

    private UserReadRepository userReadRepository;
    private ReviewCommentReadRepository reviewCommentReadRepository;
    private ReviewCommentWriteRepository reviewCommentWriteRepository;
    private EventBus eventBus;

    public void addReviewCommentReaction(AddReviewCommentReactionCommand command) {
        var user = this.userReadRepository.findById(command.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        var reviewComment = this.reviewCommentReadRepository.findById(command.reviewCommentId())
                .orElseThrow(() -> new NoSuchElementException("Review comment not found"));
        var reaction = new ReviewReaction(user, command.reaction());
        reviewComment.addReaction(reaction);
        this.reviewCommentWriteRepository.save(reviewComment);
        var event = new ReviewCommentCreatedEvent(UUID.randomUUID().toString(), LocalDateTime.now(), reviewComment);
        this.eventBus.dispatch(event);
    }

    public List<ReviewCommentResult> getReviewComments(GetReviewCommentsQuery query) {
        return this.reviewCommentReadRepository.findByReviewId(query.reviewId())
                .stream()
                .map(ReviewCommentResult::map)
                .toList();
    }
}
