package com.benjamin.cqrs.application.events.handlers;

import com.benjamin.cqrs.application.events.EventHandler;
import com.benjamin.cqrs.application.events.ReviewCreatedEvent;

public class ReviewCreatedEventHandler implements EventHandler<ReviewCreatedEvent> {
    @Override
    public void onEvent(ReviewCreatedEvent event) {
        System.out.println("saving Review on read model:" + event);
    }
}
