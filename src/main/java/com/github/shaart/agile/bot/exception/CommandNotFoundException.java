package com.github.shaart.agile.bot.exception;

public class CommandNotFoundException extends RuntimeException {

  public CommandNotFoundException(String message) {
    super(message);
  }
}
