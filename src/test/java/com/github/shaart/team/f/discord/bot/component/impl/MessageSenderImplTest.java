package com.github.shaart.team.f.discord.bot.component.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MessageSenderImplTest {

  public static final String ERROR_PREFIX = "[ERROR] ";
  private MessageSenderImpl sender;
  private MessageChannel channel;
  private MessageAction messageAction;

  @BeforeEach
  void setUp() {
    sender = new MessageSenderImpl();

    channel = mock(MessageChannel.class);
    messageAction = mock(MessageAction.class);
  }

  @Test
  void sendMessage() {
    final String message = "A message";
    final String expectedResultMessage = ERROR_PREFIX + message;

    when(channel.sendMessage(anyString()))
        .thenReturn(messageAction);
    doNothing()
        .when(messageAction).queue();

    sender.sendError(channel, message);

    //noinspection ResultOfMethodCallIgnored
    verify(channel, times(1))
        .sendMessage(eq(expectedResultMessage));
    verify(messageAction, times(1))
        .queue();
  }

  @Test
  @DisplayName("Send a null message to channel produces error message")
  void sendMessageNull() {
    final String expectedResultMessage =
        ERROR_PREFIX + "Send message called but message wasn't produced. Please check the logs.";

    when(channel.sendMessage(anyString()))
        .thenReturn(messageAction);
    doNothing()
        .when(messageAction).queue();

    sender.sendMessage(channel, null);

    //noinspection ResultOfMethodCallIgnored
    verify(channel, times(1))
        .sendMessage(eq(expectedResultMessage));
    verify(messageAction, times(1))
        .queue();
  }

  @Test
  @DisplayName("Send a longer than 2000 symbols message to channel produces trimmed message")
  void sendMessageLonger2000() {
    final String initialTemplateString = "1234567890";
    final int testLength = 3000;
    final int templateRepeatCount = testLength / initialTemplateString.length();
    final String testMessage = initialTemplateString.repeat(templateRepeatCount);

    final String postfix = "..";
    final String trimmedWithoutPostfix = testMessage.substring(0, 2000 - postfix.length());
    final String expectedResultMessage = trimmedWithoutPostfix + postfix;

    when(channel.sendMessage(anyString()))
        .thenReturn(messageAction);
    doNothing()
        .when(messageAction).queue();

    sender.sendMessage(channel, testMessage);

    //noinspection ResultOfMethodCallIgnored
    verify(channel, times(1))
        .sendMessage(eq(expectedResultMessage));
    verify(messageAction, times(1))
        .queue();
  }

  @Test
  @DisplayName("Send a common error message to channel")
  void sendError() {
    final String message = "A message";
    final String expectedResultMessage = ERROR_PREFIX + message;

    when(channel.sendMessage(anyString()))
        .thenReturn(messageAction);
    doNothing()
        .when(messageAction).queue();

    sender.sendError(channel, message);

    //noinspection ResultOfMethodCallIgnored
    verify(channel, times(1))
        .sendMessage(eq(expectedResultMessage));
    verify(messageAction, times(1))
        .queue();
  }
}