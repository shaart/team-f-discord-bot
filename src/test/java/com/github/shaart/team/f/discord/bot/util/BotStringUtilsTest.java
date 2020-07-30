package com.github.shaart.team.f.discord.bot.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BotStringUtilsTest {

  @Test
  @DisplayName("Real empty string returns true")
  void isEmptyWithEmptyString() {
    String emptyString = "";

    final boolean result = BotStringUtils.isEmpty(emptyString);

    assertTrue(result);
  }

  @Test
  @DisplayName("Null string returns true")
  void isEmptyWithNullString() {
    String nullString = null;

    //noinspection ConstantConditions
    final boolean result = BotStringUtils.isEmpty(nullString);

    assertTrue(result);
  }

  @Test
  @DisplayName("String with a space returns false")
  void isEmptyWithSpaceString() {
    String spaceString = " ";

    final boolean result = BotStringUtils.isEmpty(spaceString);

    assertFalse(result);
  }

  @Test
  @DisplayName("String with a space returns false")
  void isEmptyWithCommonString() {
    String commonString = "123abc";

    final boolean result = BotStringUtils.isEmpty(commonString);

    assertFalse(result);
  }
}