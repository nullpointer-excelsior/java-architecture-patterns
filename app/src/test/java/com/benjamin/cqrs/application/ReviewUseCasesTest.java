package com.benjamin.cqrs.application;

import com.benjamin.cqrs.application.commands.AddReviewCommand;
import com.benjamin.cqrs.application.commands.AddReviewCommentCommand;
import com.benjamin.cqrs.application.commands.AddReviewReactionCommand;
import com.benjamin.cqrs.application.queries.GetReviewsByProductQuery;
import com.benjamin.cqrs.domain.entities.*;
import com.benjamin.cqrs.domain.entities.valueobjects.Content;
import com.benjamin.cqrs.domain.ports.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ReviewUseCasesTest {

    @Mock
    private ReviewWriteRepository reviewWriteRepository;
    @Mock
    private UserReadRepository userReadRepository;
    @Mock
    private ProductReadRepository productReadRepository;
    @Mock
    private ReviewReadRepository reviewReadRepository;

    @InjectMocks
    private ReviewUseCases reviewUseCases;

    private User user;
    private Product product;
    private Review review;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("user-1", "John Doe", "jdoe@company.com");
        product = new Product("sku-123", "Product Name");
        review = Review.builder()
                .score(4.5)
                .content(new Content(user, "Great product!"))
                .product(product)
                .build();
    }

    @Test
    @DisplayName("GIVEN valid AddReviewCommand WHEN addReview THEN saves review")
    void addReview_savesReview() {
        AddReviewCommand command = AddReviewCommand.builder()
                .sku("sku-123")
                .score(4.5)
                .userId("user-1")
                .content("Great product!")
                .build();

        when(userReadRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(productReadRepository.findBySku("sku-123")).thenReturn(Optional.of(product));

        reviewUseCases.addReview(command);

        verify(reviewWriteRepository).save(any(Review.class));
    }

    @Test
    @DisplayName("GIVEN invalid user WHEN addReview THEN throws NoSuchElementException")
    void addReview_invalidUser_throwsException() {
        AddReviewCommand command = AddReviewCommand.builder()
                .sku("sku-123")
                .score(4.5)
                .userId("invalid-user")
                .content("Great product!")
                .build();

        when(userReadRepository.findById("invalid-user")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewUseCases.addReview(command))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User not found");
    }

    @Test
    @DisplayName("GIVEN valid AddReviewReactionCommand WHEN addReviewReaction THEN saves review with reaction")
    void addReviewReaction_savesReaction() {
        AddReviewReactionCommand command = new AddReviewReactionCommand("review-1", "user-1", ReactionType.LIKE);
        review = Review.builder()
                .score(4.5)
                .content(new Content(user, "Great product!"))
                .reactions(new ArrayList<>())
                .product(product)
                .build();

        when(userReadRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(reviewReadRepository.findById("review-1")).thenReturn(Optional.of(review));

        reviewUseCases.addReviewReaction(command);

        verify(reviewWriteRepository).save(review);
        assertThat(review.getReactions()).isNotEmpty();
    }

    @Test
    @DisplayName("GIVEN valid GetReviewsByProductQuery WHEN getReviewsByProduct THEN returns reviews")
    void getReviewsByProduct_returnsReviews() {
        GetReviewsByProductQuery query = new GetReviewsByProductQuery("sku-123");

        when(reviewReadRepository.findByProductSku("sku-123")).thenReturn(List.of(review));

        List<Review> result = reviewUseCases.getReviewsByProduct(query);

        assertThat(result).containsExactly(review);
    }
}