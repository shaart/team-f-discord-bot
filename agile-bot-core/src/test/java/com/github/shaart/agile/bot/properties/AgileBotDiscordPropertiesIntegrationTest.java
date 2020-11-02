package com.github.shaart.agile.bot.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.shaart.agile.bot.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AgileBotDiscordPropertiesIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private AgileBotDiscordProperties properties;

  @Test
  @DisplayName("Default test token is 'test'")
  void getToken() {
    assertEquals("test", properties.getToken());
  }

  @Test
  @DisplayName("Default test command's prefix is '!'")
  void getCommandPrefix() {
    assertEquals("!", properties.getCommandPrefix());
  }
}