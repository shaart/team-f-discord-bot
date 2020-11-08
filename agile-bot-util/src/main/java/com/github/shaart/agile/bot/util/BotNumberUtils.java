package com.github.shaart.agile.bot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BotNumberUtils {

  /**
   * Checks if the argument is an integer.
   */
  public static boolean isInteger(String argument) {
    return argument.matches("\\d+");
  }
}
