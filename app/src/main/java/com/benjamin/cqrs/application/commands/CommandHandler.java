package com.benjamin.cqrs.application.commands;

public interface CommandHandler<Command> {
    void onCommand(Command command);
}
