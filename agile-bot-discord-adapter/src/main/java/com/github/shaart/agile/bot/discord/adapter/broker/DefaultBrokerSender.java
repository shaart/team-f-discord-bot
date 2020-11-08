package com.github.shaart.agile.bot.discord.adapter.broker;

import static com.github.shaart.agile.bot.discord.adapter.config.ActiveMQConfig.MESSAGE_EVENTS_QUEUE;

import com.github.shaart.agile.bot.discord.adapter.dto.v1.EventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultBrokerSender implements BrokerSender {

  private final JmsTemplate jmsTemplate;

  public void send(EventDto event) {
    jmsTemplate.convertAndSend(MESSAGE_EVENTS_QUEUE, event);
  }
}
