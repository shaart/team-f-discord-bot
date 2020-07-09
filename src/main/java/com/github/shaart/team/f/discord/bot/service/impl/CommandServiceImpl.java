package com.github.shaart.team.f.discord.bot.service.impl;

import static java.util.Objects.isNull;

import com.github.shaart.team.f.discord.bot.command.BotCommand;
import com.github.shaart.team.f.discord.bot.dto.CommandDto;
import com.github.shaart.team.f.discord.bot.exception.CommandNotFoundException;
import com.github.shaart.team.f.discord.bot.exception.CommandValidationException;
import com.github.shaart.team.f.discord.bot.properties.TeamFDiscordBotProperties;
import com.github.shaart.team.f.discord.bot.service.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

  private final Map<String, BotCommand> commands;
  private final TeamFDiscordBotProperties properties;

  @Override
  public BotCommand findCommand(CommandDto commandDto) {
    final String commandPrefix = properties.getCommandPrefix();
    final String commandAlias = commandDto.getAlias(commandPrefix);
    final BotCommand botCommand = commands.get(commandAlias);

    if (isNull(botCommand)) {
      final String message = "Unknown command: " + commandDto.getCommand();
      throw new CommandNotFoundException(message);
    }

    return botCommand;
  }

  @Override
  public void validateArguments(BotCommand botCommand, String[] args) {
    final int minArgumentsCount = botCommand.minArgumentsCount();
    if (args.length < minArgumentsCount) {
      final String errorMessage = String.format("Incorrect arguments count. Expected at least %d.%n"
              + "Usage: %s",
          minArgumentsCount, botCommand.getUsage());
      throw new CommandValidationException(errorMessage);
    }
  }
}
