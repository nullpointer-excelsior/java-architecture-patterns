package com.benjamin.cqrs.application;

import com.benjamin.cqrs.application.commands.AddReviewCommand;
import com.benjamin.cqrs.application.commands.AddReviewCommentCommand;
import com.benjamin.cqrs.application.commands.AddReviewReactionCommand;
import com.benjamin.cqrs.application.queries.GetReviewsByProductQuery;
import com.benjamin.cqrs.application.queries.result.ReviewResult;
import com.benjamin.cqrs.domain.entities.Review;
import com.benjamin.cqrs.domain.entities.ReviewComment;
import com.benjamin.cqrs.domain.entities.ReviewReaction;
import com.benjamin.cqrs.domain.ports.repositories.*;
import com.benjamin.cqrs.domain.entities.valueobjects.Content;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
public class ReviewUseCases {

    private ReviewWriteRepository reviewWriteRepository;
    private UserReadRepository userReadRepository;
    private ProductReadRepository productReadRepository;
    private ReviewReadRepository reviewReadRepository;

    public void addReview(AddReviewCommand command) {
        var user = this.userReadRepository.findById(command.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        var product = this.productReadRepository.findBySku(command.sku())
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        var review = Review.builder()
                .score(command.score())
                .content(new Content(user, command.content()))
                .product(product)
                .build();
        this.reviewWriteRepository.save(review);
    }

    public void addReviewReaction(AddReviewReactionCommand command) {
        var user = this.userReadRepository.findById(command.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        var review = this.reviewReadRepository.findById(command.reviewId())
                .orElseThrow(() -> new NoSuchElementException("Review not found"));
        var reaction = new ReviewReaction(user, command.reaction());
        review.addReaction(reaction);
        this.reviewWriteRepository.save(review);
    }

    public void addReviewComment(AddReviewCommentCommand command) {
        var user = this.userReadRepository.findById(command.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        var review = this.reviewReadRepository.findById(command.reviewId())
                .orElseThrow(() -> new NoSuchElementException("Review not found"));
        var comment = ReviewComment.builder()
                .id(UUID.randomUUID().toString())
                .content(new Content(user, command.content()))
                .build();
        review.addComment(comment);
        this.reviewWriteRepository.save(review);
    }

    public List<ReviewResult> getReviewsByProduct(GetReviewsByProductQuery query) {
        return this.reviewReadRepository.findByProductSku(query.sku())
                .stream()
                .map(ReviewResult::map)
                .toList();
    }

}
