package com.github.shaart.team.f.discord.bot.dto;

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

  public String getAlias(String commandPrefix) {
    return getCommand().substring(commandPrefix.length());
  }

  public static CommandDto forName(String commandWithPrefix) {
    return CommandDto.builder()
        .command(commandWithPrefix)
        .build();
  }
}
