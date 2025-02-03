package com.benjamin.cqrs.application.events.handlers;

import com.benjamin.cqrs.application.events.EventHandler;
import com.benjamin.cqrs.application.events.ReviewCommentCreatedEvent;

public class ReviewCommentCreatedEventHandler implements EventHandler<ReviewCommentCreatedEvent> {
    @Override
    public void onEvent(ReviewCommentCreatedEvent event) {
        System.out.println("saving ReviewComment on read model:" + event);
    }
}
