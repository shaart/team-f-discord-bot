package com.github.shaart.team.f.discord.bot.component.impl;

import static java.util.Objects.isNull;

import com.github.shaart.team.f.discord.bot.component.MessageSender;
import com.github.shaart.team.f.discord.bot.dto.ChannelDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageSenderImpl implements MessageSender {

  private static final String NO_MESSAGE_PRODUCED_ERROR =
      "Send message called but message wasn't produced. Please check the logs.";
  private static final int DISCORD_MESSAGE_LENGTH_LIMIT = 2000;
  private static final String OVERFLOW_POSTFIX = "..";

  @Override
  public void sendMessage(ChannelDto channel, String message) {
    if (isNull(message)) {
      sendError(channel, NO_MESSAGE_PRODUCED_ERROR);
      return;
    }
    send(channel, message);
  }

  @Override
  public void sendError(ChannelDto channel, String message) {
    send(channel, "[ERROR] " + message);
  }

  private void send(ChannelDto channel, String message) {
    String resultMessage;
    if (message.length() > DISCORD_MESSAGE_LENGTH_LIMIT) {
      log.warn("A message is larger than {} symbols. It will be trimmed. "
              + "Initial message (length = {}): {}",
          DISCORD_MESSAGE_LENGTH_LIMIT, message.length(), message);
      final int endIndex = DISCORD_MESSAGE_LENGTH_LIMIT - OVERFLOW_POSTFIX.length();
      final String shortedMessage = message.substring(0, endIndex);
      resultMessage = shortedMessage + OVERFLOW_POSTFIX;
    } else {
      resultMessage = message;
    }
    channel.sendMessage(resultMessage);
  }
}
