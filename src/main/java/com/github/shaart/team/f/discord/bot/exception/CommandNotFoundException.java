package com.github.shaart.team.f.discord.bot.exception;

public class CommandNotFoundException extends RuntimeException {

  public CommandNotFoundException(String message) {
    super(message);
  }
}
