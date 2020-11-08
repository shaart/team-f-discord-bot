package com.github.shaart.agile.bot.discord.adapter.broker;

import com.github.shaart.agile.bot.discord.adapter.dto.v1.EventDto;

public interface BrokerSender {

  void send(EventDto event);
}
