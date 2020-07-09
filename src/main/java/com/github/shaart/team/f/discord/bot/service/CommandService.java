package com.github.shaart.team.f.discord.bot.service;

import com.github.shaart.team.f.discord.bot.command.BotCommand;
import com.github.shaart.team.f.discord.bot.dto.CommandDto;

public interface CommandService {

  BotCommand findCommand(CommandDto commandDto);

  void validateArguments(BotCommand botCommand, String[] args);
}
