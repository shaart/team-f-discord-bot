package com.github.shaart.team.f.discord.bot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "team.f.discord.bot", ignoreUnknownFields = false)
public class TeamFDiscordBotProperties {

  /**
   * Discord bot's token for work.
   */
  private String token;

  /**
   * Prefix for a message to became a bot's command. "!" by default.
   */
  private String commandPrefix;
}
