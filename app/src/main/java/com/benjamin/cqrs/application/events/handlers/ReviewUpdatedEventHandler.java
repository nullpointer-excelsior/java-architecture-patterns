package com.benjamin.cqrs.application.events.handlers;

import com.benjamin.cqrs.application.events.EventHandler;
import com.benjamin.cqrs.application.events.ReviewUpdatedEvent;

public class ReviewUpdatedEventHandler implements EventHandler<ReviewUpdatedEvent> {
    @Override
    public void onEvent(ReviewUpdatedEvent event) {
        System.out.println("Updating Review on read model:" + event);
    }
}
