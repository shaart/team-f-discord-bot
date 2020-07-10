package com.github.shaart.team.f.discord.bot.command;

import com.github.shaart.team.f.discord.bot.component.MessageSender;
import com.github.shaart.team.f.discord.bot.properties.TeamFDiscordBotProperties;

/**
 * Abstract command class with state for all commands.
 */
public abstract class AbstractBotCommand implements BotCommand {

  protected final TeamFDiscordBotProperties applicationProperties;

  protected final String commandPrefix;
  protected final MessageSender messageSender;

  /**
   * Create a command core part with command prefix from properties and dependencies.
   *
   * @param applicationProperties application properties to set
   * @param messageSender         message sender to set
   */
  public AbstractBotCommand(TeamFDiscordBotProperties applicationProperties,
      MessageSender messageSender) {

    this.applicationProperties = applicationProperties;
    this.messageSender = messageSender;
    this.commandPrefix = applicationProperties.getCommandPrefix();
  }
}
