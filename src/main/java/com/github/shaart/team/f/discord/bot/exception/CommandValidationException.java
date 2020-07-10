package com.github.shaart.team.f.discord.bot.exception;

public class CommandValidationException extends RuntimeException {

  public CommandValidationException(String message) {
    super(message);
  }
}
