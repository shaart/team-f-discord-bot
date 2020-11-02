package com.github.shaart.agile.bot.command.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.shaart.agile.bot.AbstractIntegrationTest;
import com.github.shaart.agile.bot.dto.ChannelDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PingCommandIntegrationTest extends AbstractIntegrationTest {

  public static final int ONCE = 1;

  @Autowired
  private PingCommand pingCommand;

  @Test
  @DisplayName("Run !ping with no args produces 'Pong!' message")
  void runNoArgs() {
    pingCommand.run(messageReceivedEvent);

    verify(messageSender, times(ONCE))
        .sendMessage(any(ChannelDto.class), eq("Pong!"));
  }
}