package com.github.shaart.agile.bot.discord.adapter.listener;

import static net.logstash.logback.marker.Markers.append;

import com.github.shaart.agile.bot.discord.adapter.broker.BrokerSender;
import com.github.shaart.agile.bot.discord.adapter.dto.v1.EventDto;
import com.github.shaart.agile.bot.discord.adapter.dto.v1.MessageDto;
import com.github.shaart.agile.bot.discord.adapter.mapper.impl.discord.v1.EventMapper;
import com.github.shaart.agile.bot.discord.adapter.properties.AgileBotDiscordProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AgileBotDiscordListener extends ListenerAdapter {

  public static final String MDC_EVENT_ID_FIELD = "global_event_id";

  private final AgileBotDiscordProperties properties;
  private final EventMapper eventMapper;
  private final BrokerSender brokerSender;

  /**
   * Logic for received messages in channels.
   *
   * @param event an event
   */
  @Override
  public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
    try {
      MDC.put(MDC_EVENT_ID_FIELD, event.getMessageId());
      handleEvent(event);
      MDC.remove(MDC_EVENT_ID_FIELD);
    } catch (Exception e) {
      log.error("An unexpected exception occurred", e);
    }
  }

  private void handleEvent(@Nonnull MessageReceivedEvent receivedEvent) {
    EventDto event = eventMapper.toInternalModel(receivedEvent);
    if (event.getAuthor().isBot()) {
      return;
    }
    MessageDto message = event.getMessage();
    String content = event.getContent();
    if (!content.startsWith(properties.getCommandPrefix())) {
      return;
    }

    log.info("Got a message from '{}' at '{}' ({}) in channel '{}'", message.getAuthorName(),
        event.getServerName(), event.getServerId(), event.getChannel().getName());
    log.trace("The message is: '{}'", content);

    log.info(append("author_name", message.getAuthorName())
            .and(append("server_name", event.getServerName()))
            .and(append("server_id", event.getServerId()))
            .and(append("channel_name", event.getChannel().getName())),
        "Got a message from user at server (with server id) in channel");

    log.info("Sending event to message broker");
    brokerSender.send(event);
    log.info("Event successfully sent to message broker");
  }
}
