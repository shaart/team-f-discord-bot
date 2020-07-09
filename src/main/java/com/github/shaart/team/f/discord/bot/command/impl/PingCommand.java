package com.github.shaart.team.f.discord.bot.command.impl;

import com.github.shaart.team.f.discord.bot.command.AbstractBotCommand;
import com.github.shaart.team.f.discord.bot.component.MessageSender;
import com.github.shaart.team.f.discord.bot.properties.TeamFDiscordBotProperties;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(PingCommand.PING_COMMAND_ALIAS)
public class PingCommand extends AbstractBotCommand {

  public static final String PING_COMMAND_ALIAS = "ping";

  @Autowired
  public PingCommand(TeamFDiscordBotProperties properties, MessageSender messageSender) {
    super(properties, messageSender);
  }

  @Override
  public int minArgumentsCount() {
    return 0;
  }

  @Override
  public String getUsage() {
    return ""
        + "```"
        + commandPrefix + PING_COMMAND_ALIAS
        + "```";
  }

  @Override
  public void run(MessageReceivedEvent event, String... args) {
    final MessageChannel channel = event.getChannel();
    messageSender.sendMessage(channel, "Pong!");
  }
}
