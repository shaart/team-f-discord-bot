package com.github.shaart.agile.bot.command;

import com.github.shaart.agile.bot.component.MessageSender;
import com.github.shaart.agile.bot.properties.AgileBotDiscordProperties;

/**
 * Abstract command class with state for all commands.
 */
public abstract class AbstractBotCommand implements BotCommand {

  protected final AgileBotDiscordProperties applicationProperties;

  protected final String commandPrefix;
  protected final MessageSender messageSender;

  /**
   * Create a command core part with command prefix from properties and dependencies.
   *
   * @param applicationProperties application properties to set
   * @param messageSender         message sender to set
   */
  public AbstractBotCommand(AgileBotDiscordProperties applicationProperties,
      MessageSender messageSender) {

    this.applicationProperties = applicationProperties;
    this.messageSender = messageSender;
    this.commandPrefix = applicationProperties.getCommandPrefix();
  }
}
