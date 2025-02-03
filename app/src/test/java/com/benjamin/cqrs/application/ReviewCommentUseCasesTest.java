package com.benjamin.cqrs.application;

import com.benjamin.cqrs.application.commands.AddReviewCommentReactionCommand;
import com.benjamin.cqrs.application.events.ReviewCommentCreatedEvent;
import com.benjamin.cqrs.application.events.ReviewUpdatedEvent;
import com.benjamin.cqrs.application.ports.integration.EventBus;
import com.benjamin.cqrs.application.queries.GetReviewCommentsQuery;
import com.benjamin.cqrs.application.queries.result.ReviewCommentResult;
import com.benjamin.cqrs.domain.entities.ReactionType;
import com.benjamin.cqrs.domain.entities.ReviewComment;
import com.benjamin.cqrs.domain.entities.ReviewReaction;
import com.benjamin.cqrs.domain.entities.User;
import com.benjamin.cqrs.domain.entities.valueobjects.Content;
import com.benjamin.cqrs.domain.ports.repositories.ReviewCommentReadRepository;
import com.benjamin.cqrs.domain.ports.repositories.ReviewCommentWriteRepository;
import com.benjamin.cqrs.domain.ports.repositories.UserReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ReviewCommentUseCasesTest {

    @Mock
    private UserReadRepository userReadRepository;

    @Mock
    private ReviewCommentReadRepository reviewCommentReadRepository;

    @Mock
    private ReviewCommentWriteRepository reviewCommentWriteRepository;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private ReviewCommentUseCases reviewCommentUseCases;

    private AddReviewCommentReactionCommand command;
    private User user;
    private ReviewComment reviewComment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = mock(User.class);
        reviewComment = mock(ReviewComment.class);
        command = new AddReviewCommentReactionCommand("commentId", "userId", ReactionType.LIKE);
    }

    @Test
    @DisplayName("GIVEN valid user and review comment WHEN add reaction THEN save review comment AND dispatch ReviewCommentCreatedEvent")
    void addReviewCommentReaction_savesReviewReaction() {
        when(userReadRepository.findById("userId")).thenReturn(Optional.of(user));
        when(reviewCommentReadRepository.findById("commentId")).thenReturn(Optional.of(reviewComment));

        reviewCommentUseCases.addReviewCommentReaction(command);

        verify(reviewComment).addReaction(any(ReviewReaction.class));
        verify(reviewCommentWriteRepository).save(reviewComment);
        verify(eventBus).dispatch(any(ReviewCommentCreatedEvent.class));
    }

    @Test
    @DisplayName("GIVEN non-existent user WHEN add reaction THEN throw NoSuchElementException")
    void addReviewCommentReaction_throwsNoSuchElementException_UserNotFound() {
        when(userReadRepository.findById("userId")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewCommentUseCases.addReviewCommentReaction(command))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User not found");
    }

    @Test
    @DisplayName("GIVEN non-existent review comment WHEN add reaction THEN throw NoSuchElementException")
    void addReviewCommentReaction_throwsNoSuchElementException_ReviewNotFound() {
        when(userReadRepository.findById("userId")).thenReturn(Optional.of(user));
        when(reviewCommentReadRepository.findById("commentId")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewCommentUseCases.addReviewCommentReaction(command))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Review comment not found");
    }

    @Test
    @DisplayName("GIVEN review id WHEN get review comments THEN return list of comments")
    void getReviewComments_returnsReviewComments() {
        GetReviewCommentsQuery query = new GetReviewCommentsQuery("reviewId");
        ReviewComment comment1 = ReviewComment.builder()
                .content(new Content(new User("", "john", ""), "good"))
                .id("id1")
                .reactions(List.of())
                .build();
        ReviewComment comment2 = ReviewComment.builder()
                .content(new Content(new User("", "jack", ""), "useful"))
                .id("id2")
                .reactions(List.of())
                .build();
        List<ReviewComment> comments = List.of(comment1, comment2);
        when(reviewCommentReadRepository.findByReviewId("reviewId")).thenReturn(comments);

        List<ReviewCommentResult> result = reviewCommentUseCases.getReviewComments(query);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo("id1");
        assertThat(result.get(0).content().content()).isEqualTo("good");
        assertThat(result.get(0).reactions()).isEmpty();
        assertThat(result.get(1).id()).isEqualTo("id2");
        assertThat(result.get(1).content().content()).isEqualTo("useful");
        assertThat(result.get(1).reactions()).isEmpty();
    }
}
