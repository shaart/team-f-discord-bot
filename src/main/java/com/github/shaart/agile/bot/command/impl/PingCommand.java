package com.github.shaart.agile.bot.command.impl;

import com.github.shaart.agile.bot.command.AbstractBotCommand;
import com.github.shaart.agile.bot.component.MessageSender;
import com.github.shaart.agile.bot.dto.ChannelDto;
import com.github.shaart.agile.bot.dto.EventDto;
import com.github.shaart.agile.bot.properties.AgileBotDiscordProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(PingCommand.PING_COMMAND_ALIAS)
public class PingCommand extends AbstractBotCommand {

  public static final String PING_COMMAND_ALIAS = "ping";

  @Autowired
  public PingCommand(AgileBotDiscordProperties properties, MessageSender messageSender) {
    super(properties, messageSender);
  }

  @Override
  public int minArgumentsCount() {
    return 0;
  }

  @Override
  public String getAlias() {
    return PING_COMMAND_ALIAS;
  }

  @Override
  public String getDescription() {
    return "Check bot";
  }

  @Override
  public String getUsage() {
    return ""
        + "```"
        + commandPrefix + PING_COMMAND_ALIAS
        + "```";
  }

  @Override
  public void run(EventDto event, String... args) {
    final ChannelDto channel = event.getChannel();
    messageSender.sendMessage(channel, "Pong!");
  }
}
