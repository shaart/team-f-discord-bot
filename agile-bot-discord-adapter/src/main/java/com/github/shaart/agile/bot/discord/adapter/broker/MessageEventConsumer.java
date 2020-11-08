package com.github.shaart.agile.bot.discord.adapter.broker;

import static com.github.shaart.agile.bot.discord.adapter.config.ActiveMQConfig.MESSAGE_EVENTS_QUEUE;

import com.github.shaart.agile.bot.discord.adapter.dto.v1.EventDto;
import com.github.shaart.agile.bot.discord.adapter.enums.EventSource;
import com.github.shaart.agile.bot.discord.adapter.messenger.DiscordResponseMessenger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageEventConsumer {

  private final DiscordResponseMessenger discordResponseMessenger;

  @JmsListener(destination = MESSAGE_EVENTS_QUEUE)
  public void receiveMessage(@Payload EventDto event, @Headers MessageHeaders headers) {
    log.info("Received event: {}", event);
    log.trace("Event's headers: {}", headers);

    EventSource sourceType = event.getSourceType();
    if (sourceType == EventSource.DISCORD) {
      discordResponseMessenger.sendResponse(event);
    } else {
      throw new IllegalArgumentException("Unknown source type: " + sourceType);
    }
  }

}
