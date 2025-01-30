package com.benjamin.cqrs.application;

import com.benjamin.cqrs.application.commands.AddReviewCommentReactionCommand;
import com.benjamin.cqrs.application.queries.GetReviewCommentsQuery;
import com.benjamin.cqrs.domain.entities.ReactionType;
import com.benjamin.cqrs.domain.entities.ReviewComment;
import com.benjamin.cqrs.domain.entities.ReviewReaction;
import com.benjamin.cqrs.domain.entities.User;
import com.benjamin.cqrs.domain.ports.repositories.ReviewCommentReadRepository;
import com.benjamin.cqrs.domain.ports.repositories.ReviewCommentWriteRepository;
import com.benjamin.cqrs.domain.ports.repositories.UserReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    @DisplayName("GIVEN valid user and review comment WHEN add reaction THEN save review comment")
    void addReviewCommentReaction_savesReviewReaction() {
        when(userReadRepository.findById("userId")).thenReturn(Optional.of(user));
        when(reviewCommentReadRepository.findById("commentId")).thenReturn(Optional.of(reviewComment));

        reviewCommentUseCases.addReviewCommentReaction(command);

        verify(reviewComment).addReaction(any(ReviewReaction.class));
        verify(reviewCommentWriteRepository).save(reviewComment);
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
        List<ReviewComment> comments = List.of(mock(ReviewComment.class));
        when(reviewCommentReadRepository.findByReviewId("reviewId")).thenReturn(comments);

        List<ReviewComment> result = reviewCommentUseCases.getReviewComments(query);

        assertThat(result).isEqualTo(comments);
    }
}
