package com.benjamin.cqrs.application.ports.integration;

import com.benjamin.cqrs.application.commands.Command;

public interface CommandBus {
    void dispatch(Command command);
}
