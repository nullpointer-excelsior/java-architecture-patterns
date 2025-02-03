package com.benjamin.cqrs.application;

import com.benjamin.cqrs.application.commands.AddReviewCommentReactionCommand;
import com.benjamin.cqrs.application.queries.GetReviewCommentsQuery;
import com.benjamin.cqrs.application.queries.result.ReviewCommentResult;
import com.benjamin.cqrs.domain.entities.ReviewComment;
import com.benjamin.cqrs.domain.entities.ReviewReaction;
import com.benjamin.cqrs.domain.ports.repositories.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class ReviewCommentUseCases {

    private UserReadRepository userReadRepository;
    private ReviewCommentReadRepository reviewCommentReadRepository;
    private ReviewCommentWriteRepository reviewCommentWriteRepository;

    public void addReviewCommentReaction(AddReviewCommentReactionCommand command) {
        var user = this.userReadRepository.findById(command.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        var reviewComment = this.reviewCommentReadRepository.findById(command.reviewCommentId())
                .orElseThrow(() -> new NoSuchElementException("Review comment not found"));
        var reaction = new ReviewReaction(user, command.reaction());
        reviewComment.addReaction(reaction);
        this.reviewCommentWriteRepository.save(reviewComment);
    }

    public List<ReviewCommentResult> getReviewComments(GetReviewCommentsQuery query) {
        return this.reviewCommentReadRepository.findByReviewId(query.reviewId())
                .stream()
                .map(ReviewCommentResult::map)
                .toList();
    }
}
