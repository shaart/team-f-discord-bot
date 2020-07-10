package com.github.shaart.team.f.discord.bot.service;

import com.github.shaart.team.f.discord.bot.command.BotCommand;
import com.github.shaart.team.f.discord.bot.dto.CommandDto;

import java.util.List;

public interface CommandService {

  int DEFAULT_PAGE_NUMBER = 1;
  int DEFAULT_PAGE_SIZE = 10;

  BotCommand findCommand(CommandDto commandDto);

  void validateArguments(BotCommand botCommand, String[] args);

  default List<BotCommand> getCommandsPage() {
    return getCommandsPage(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
  }

  default List<BotCommand> getCommandsPage(int pageNumber) {
    return getCommandsPage(pageNumber, DEFAULT_PAGE_SIZE);
  }

  List<BotCommand> getCommandsPage(int pageNumber, int pageSize);
}
