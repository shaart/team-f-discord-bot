package com.github.shaart.team.f.discord.bot.service;

import com.github.shaart.team.f.discord.bot.command.BotCommand;
import com.github.shaart.team.f.discord.bot.dto.CommandDto;
import com.github.shaart.team.f.discord.bot.exception.CommandNotFoundException;
import com.github.shaart.team.f.discord.bot.exception.CommandValidationException;

import java.util.List;

/**
 * Service for working with commands.
 */
public interface CommandService {

  int DEFAULT_PAGE_NUMBER = 1;
  int DEFAULT_PAGE_SIZE = 10;

  /**
   * Find command by command's alias.
   *
   * @param commandDto DTO with alias
   * @return found command
   * @throws CommandNotFoundException if no command found by alias
   */
  BotCommand findCommand(CommandDto commandDto);

  /**
   * Validate arguments for command.
   *
   * @param botCommand target command
   * @param args       arguments to check
   * @throws CommandValidationException if arguments don't meet expectations
   */
  void validateArguments(BotCommand botCommand, String[] args);

  /**
   * Get list of commands with default page number and default page size.
   */
  default List<BotCommand> getCommandsPage() {
    return getCommandsPage(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
  }

  /**
   * Get Nth list of commands with default page size.
   */
  default List<BotCommand> getCommandsPage(int pageNumber) {
    return getCommandsPage(pageNumber, DEFAULT_PAGE_SIZE);
  }

  /**
   * Get list of commands by number and with a size.
   *
   * @param pageNumber page number. Minimal - 1. If < 1 then will be processed as 1.
   * @param pageSize   page size. Minimal - 1. If < 1 then will be processed as 1.
   * @return found page or empty list
   */
  List<BotCommand> getCommandsPage(int pageNumber, int pageSize);
}
