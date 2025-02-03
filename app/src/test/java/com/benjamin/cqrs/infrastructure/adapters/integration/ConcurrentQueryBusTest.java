package com.benjamin.cqrs.infrastructure.adapters.integration;

import com.benjamin.cqrs.application.queries.GetReviewCommentsQuery;
import com.benjamin.cqrs.application.queries.GetReviewsByProductQuery;
import com.benjamin.cqrs.application.queries.Query;
import com.benjamin.cqrs.application.queries.Result;
import com.benjamin.cqrs.application.queries.handlers.GetReviewCommentsQueryHandler;
import com.benjamin.cqrs.application.queries.handlers.GetReviewsByProductQueryHandler;
import com.benjamin.cqrs.application.queries.result.*;
import com.benjamin.cqrs.domain.entities.ReactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ConcurrentQueryBusTest {

    @Mock
    private GetReviewCommentsQueryHandler getReviewCommentsQueryHandler;
    @Mock
    private GetReviewsByProductQueryHandler getReviewsByProductQueryHandler;

    private ConcurrentQueryBus concurrentQueryBus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        concurrentQueryBus = ConcurrentQueryBus.builder()
                .getReviewCommentsQueryHandler(getReviewCommentsQueryHandler)
                .getReviewsByProductQueryHandler(getReviewsByProductQueryHandler)
                .build();
    }

    @Test
    @DisplayName("GIVEN a GetReviewCommentsQuery WHEN query THEN return review comments")
    void givenAGetReviewCommentsQueryWhenQueryThenReturnReviewComments() {
        // Arrange
        GetReviewCommentsQuery query = new GetReviewCommentsQuery("reviewId");
        List<ReviewCommentResult> expectedResult = List.of(
                new ReviewCommentResult(
                        "1",
                        new ContentResult("Alice Johnson", "Great product, highly recommended!"),
                        List.of(
                                new ReviewReactionResult("Bob Smith", ReactionType.LIKE),
                                new ReviewReactionResult("Charlie Davis", ReactionType.DISLIKE)
                        )
                ),
                new ReviewCommentResult(
                        "2",
                        new ContentResult("David Martinez", "Not what I expected, but still decent."),
                        List.of(
                                new ReviewReactionResult("Emma Wilson", ReactionType.DISLIKE)
                        )
                )
        );
        when(getReviewCommentsQueryHandler.onQuery(query)).thenReturn(expectedResult);
        // Act
        CompletableFuture<List<Result>> futureResult = concurrentQueryBus.query(query, Result.class);
        List<Result> actualResult = futureResult.join();
        // Asserts
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("GIVEN a GetReviewsByProductQuery WHEN query THEN return reviews by product")
    void givenAGetReviewsByProductQueryWhenQueryThenReturnReviewsByProduct() {
        // Arrange
        GetReviewsByProductQuery query = new GetReviewsByProductQuery("sku");
        List<ReviewResult> expectedResult = List.of(
                ReviewResult.builder()
                        .score(4.5)
                        .product(new ProductResult("SKU123", "Wireless Headphones"))
                        .content(new ContentResult("Alice Johnson", "Amazing sound quality and battery life."))
                        .build(),
                ReviewResult.builder()
                        .score(3.0)
                        .product(new ProductResult("SKU456", "Smartphone Case"))
                        .content(new ContentResult("David Martinez", "Fits well but could be more durable."))
                        .build()
        );
        when(getReviewsByProductQueryHandler.onQuery(query)).thenReturn(expectedResult);
        // Act
        CompletableFuture<List<Result>> futureResult = concurrentQueryBus.query(query, Result.class);
        List<Result> actualResult = futureResult.join();
        // Asserts
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("GIVEN an unknown query WHEN query THEN throw IllegalArgumentException")
    void givenAnUnknownQueryWhenQueryThenThrowIllegalArgumentException() {
        // Arrange
        Query unknownQuery = new Query() {};
        // Act & Asserts
        assertThatThrownBy(() -> concurrentQueryBus.query(unknownQuery, Result.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown query");
    }
}
