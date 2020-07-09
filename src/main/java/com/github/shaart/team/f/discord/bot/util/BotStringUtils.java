package com.github.shaart.team.f.discord.bot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BotStringUtils {

  /**
   * Returns true if the input string is the empty string (null-safe).
   *
   * @param value String to be checked
   * @return true if String is null or empty
   */
  public static boolean isEmpty(String value) {
    return (value == null) || value.isEmpty();
  }
}
