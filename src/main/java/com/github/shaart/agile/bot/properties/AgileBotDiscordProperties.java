package com.github.shaart.agile.bot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "agile.bot.discord", ignoreUnknownFields = false)
public class AgileBotDiscordProperties {

  /**
   * Discord bot's token for work.
   */
  private String token;

  /**
   * Prefix for a message to became a bot's command. "!" by default.
   */
  private String commandPrefix;
}
