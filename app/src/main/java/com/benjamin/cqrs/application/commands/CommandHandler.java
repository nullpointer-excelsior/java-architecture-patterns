package com.benjamin.cqrs.application.commands;


public interface CommandHandler<T extends Command> {
   void onCommand(T command);
}
