package com.github.shaart.agile.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class CommandDto {

  private final String command;
  private final String[] arguments;

  /**
   * Get command name without prefix.
   */
  public String getAlias(String commandPrefix) {
    return getCommand().substring(commandPrefix.length());
  }

  /**
   * Create command with full name (like "!command").
   */
  public static CommandDto forName(String commandWithPrefix) {
    return CommandDto.builder()
        .command(commandWithPrefix)
        .build();
  }
}
