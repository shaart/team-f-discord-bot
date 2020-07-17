package com.github.shaart.team.f.discord.bot.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.shaart.team.f.discord.bot.AbstractIntegrationTest;
import com.github.shaart.team.f.discord.bot.command.BotCommand;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

class TeamFDiscordBotListenerIntegrationTest extends AbstractIntegrationTest {

  public static final String MONOFORMATTED_ONE_DIGIT_FROM_ONE_TO_FIVE = "`[1-5]{1}`";

  @Autowired
  private TeamFDiscordBotListener listener;

  @Autowired
  private Map<String, BotCommand> commands;

  @Test
  @DisplayName("On received '!ping' message produces 'Pong!' command call")
  void onMessageReceivedPing() {
    when(message.getAuthor())
        .thenReturn(testUser);
    when(message.getContentRaw())
        .thenReturn("!ping");

    listener.onMessageReceived(messageReceivedEvent);

    verify(messageSender, only())
        .sendMessage(any(MessageChannel.class), eq("Pong!"));
  }

  @Test
  @DisplayName("On received '!ping ab 12' message produces 'Ping' command call")
  void onMessageReceivedPingWithArgs() {
    when(message.getAuthor())
        .thenReturn(testUser);
    when(message.getContentRaw())
        .thenReturn("!ping ab 12");

    listener.onMessageReceived(messageReceivedEvent);

    verify(messageSender, only())
        .sendMessage(any(MessageChannel.class), eq("Pong!"));
  }

  @Test
  @DisplayName("On received '!help' message produces 'Help' command call")
  void onMessageReceivedHelp() {
    when(message.getAuthor())
        .thenReturn(testUser);
    when(message.getContentRaw())
        .thenReturn("!help");

    listener.onMessageReceived(messageReceivedEvent);

    verify(messageSender, only())
        .sendMessage(any(MessageChannel.class), contains("help - Commands help"));
  }

  @Test
  @DisplayName("On received '!help random' message produces 'Help' with 'Random' description")
  void onMessageReceivedHelpRandomDescription() {
    when(message.getAuthor())
        .thenReturn(testUser);
    when(message.getContentRaw())
        .thenReturn("!help random");

    listener.onMessageReceived(messageReceivedEvent);

    final BotCommand commandRandom = commands.get("random");

    verify(messageSender, only())
        .sendMessage(any(MessageChannel.class), contains(commandRandom.getUsage()));
  }

  @Test
  @DisplayName("On received '!random 1 5' message produces 'Random' command call")
  void onMessageReceivedRandom() {
    when(message.getAuthor())
        .thenReturn(testUser);
    when(message.getContentRaw())
        .thenReturn("!random 1 5");

    listener.onMessageReceived(messageReceivedEvent);

    verify(messageSender, only())
        .sendMessage(any(MessageChannel.class), matches(MONOFORMATTED_ONE_DIGIT_FROM_ONE_TO_FIVE));
  }
}