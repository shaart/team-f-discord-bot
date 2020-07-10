package com.github.shaart.team.f.discord.bot.service.impl;

import static java.util.Objects.isNull;

import com.github.shaart.team.f.discord.bot.command.BotCommand;
import com.github.shaart.team.f.discord.bot.dto.CommandDto;
import com.github.shaart.team.f.discord.bot.exception.CommandNotFoundException;
import com.github.shaart.team.f.discord.bot.exception.CommandValidationException;
import com.github.shaart.team.f.discord.bot.properties.TeamFDiscordBotProperties;
import com.github.shaart.team.f.discord.bot.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class CommandServiceImpl implements CommandService {

  private final SortedMap<String, BotCommand> commands;
  private final TeamFDiscordBotProperties properties;

  @Autowired
  public CommandServiceImpl(Map<String, BotCommand> commands,
      TeamFDiscordBotProperties properties) {
    this.commands = new TreeMap<>(commands);
    this.properties = properties;
  }

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

  @Override
  public List<BotCommand> getCommandsPage(int pageNumber, int pageSize) {
    pageNumber = Math.max(pageNumber, 1);
    pageSize = Math.max(pageSize, 1);
    final int n = pageNumber - 1;
    return commands.entrySet()
        .stream()
        .skip(n * pageSize)
        .limit(pageSize)
        .map(Entry::getValue)
        .collect(Collectors.toList());
  }
}
