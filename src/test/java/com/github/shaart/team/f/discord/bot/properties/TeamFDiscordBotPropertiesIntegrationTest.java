package com.github.shaart.team.f.discord.bot.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.shaart.team.f.discord.bot.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TeamFDiscordBotPropertiesIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private TeamFDiscordBotProperties properties;

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