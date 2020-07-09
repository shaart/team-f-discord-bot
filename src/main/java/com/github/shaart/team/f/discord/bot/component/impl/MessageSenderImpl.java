package com.github.shaart.team.f.discord.bot.component.impl;

import com.github.shaart.team.f.discord.bot.component.MessageSender;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class MessageSenderImpl implements MessageSender {

  @Override
  public void sendMessage(MessageChannel channel, String message) {
    send(channel, message);
  }

  @Override
  public void sendError(MessageChannel channel, String message) {
    send(channel, "[ERROR] " + message);
  }

  private void send(MessageChannel channel, String message) {
    channel.sendMessage(message).queue();
  }
}
