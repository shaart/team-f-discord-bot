package com.github.shaart.team.f.discord.bot.command;

import com.github.shaart.team.f.discord.bot.component.MessageSender;
import com.github.shaart.team.f.discord.bot.properties.TeamFDiscordBotProperties;

public abstract class AbstractBotCommand implements BotCommand {

  protected final TeamFDiscordBotProperties applicationProperties;

  protected final String commandPrefix;
  protected final MessageSender messageSender;

  public AbstractBotCommand(TeamFDiscordBotProperties applicationProperties,
      MessageSender messageSender) {

    this.applicationProperties = applicationProperties;
    this.messageSender = messageSender;
    this.commandPrefix = applicationProperties.getCommandPrefix();
  }
}
