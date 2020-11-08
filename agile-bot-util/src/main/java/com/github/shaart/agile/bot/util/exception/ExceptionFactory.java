package com.github.shaart.agile.bot.util.exception;

public class ExceptionFactory {

  public static UnsupportedOperationException newNotImplementedException() {
    return new UnsupportedOperationException("Not implemented");
  }
}
