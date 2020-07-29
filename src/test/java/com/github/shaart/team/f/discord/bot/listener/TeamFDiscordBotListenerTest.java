package com.github.shaart.team.f.discord.bot.listener;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.github.shaart.team.f.discord.bot.command.BotCommand;
import com.github.shaart.team.f.discord.bot.component.MessageSender;
import com.github.shaart.team.f.discord.bot.component.Tokenizer;
import com.github.shaart.team.f.discord.bot.dto.CommandDto;
import com.github.shaart.team.f.discord.bot.exception.CommandNotFoundException;
import com.github.shaart.team.f.discord.bot.exception.CommandValidationException;
import com.github.shaart.team.f.discord.bot.properties.TeamFDiscordBotProperties;
import com.github.shaart.team.f.discord.bot.service.CommandService;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamFDiscordBotListenerTest {

  private static final int ONCE = 1;

  @Mock
  private User testUser;

  @Mock
  private MessageChannel messageChannel;

  @Mock
  private MessageSender messageSender;

  @Mock
  private MessageReceivedEvent messageReceivedEvent;

  @Mock
  private Message message;

  @Mock
  private Tokenizer tokenizer;

  @Mock
  private CommandService commandService;

  @Mock
  private TeamFDiscordBotProperties properties;

  @InjectMocks
  private TeamFDiscordBotListener listener;

  @BeforeEach
  void setUp() {
    lenient()
        .when(messageReceivedEvent.getChannel())
        .thenReturn(messageChannel);
    lenient()
        .when(messageReceivedEvent.getMessage())
        .thenReturn(message);

    when(messageReceivedEvent.getAuthor())
        .thenAnswer(invocation -> message.getAuthor());
    when(message.getAuthor())
        .thenReturn(testUser);

    lenient()
        .when(testUser.isBot())
        .thenReturn(Boolean.FALSE);
    lenient()
        .when(testUser.getName())
        .thenReturn("Test user");

    lenient()
        .when(properties.getCommandPrefix())
        .thenReturn("!");
  }

  @Test
  @DisplayName("Do nothing on received message from bot")
  void onMessageReceivedFromBot() {
    when(testUser.isBot())
        .thenReturn(Boolean.TRUE);

    listener.onMessageReceived(messageReceivedEvent);

    verifyNoInteractions(tokenizer, commandService, messageSender);
  }

  @Test
  @DisplayName("Do nothing on received 'random message' message with no command prefix")
  void onMessageReceivedWithNoPrefix() {
    when(message.getContentRaw())
        .thenReturn("random message");

    listener.onMessageReceived(messageReceivedEvent);

    verifyNoInteractions(tokenizer, commandService, messageSender);
  }

  @Test
  @DisplayName("Arguments validation fail produces an exception that sends as error message")
  void onMessageReceivedWithArgsValidationsFail() {
    final String testContent = "!random";
    when(message.getContentRaw())
        .thenReturn(testContent);

    final CommandDto commandDto = mock(CommandDto.class);
    final BotCommand botCommand = mock(BotCommand.class);

    when(tokenizer.toCommand(testContent))
        .thenReturn(commandDto);
    when(commandService.findCommand(commandDto))
        .thenReturn(botCommand);

    final String[] testArguments = new String[0];
    when(commandDto.getArguments())
        .thenReturn(testArguments);

    final String exceptionMessage = "A test exception message";
    final CommandValidationException validationException =
        new CommandValidationException(exceptionMessage);
    doThrow(validationException)
        .when(commandService).validateArguments(botCommand, testArguments);

    listener.onMessageReceived(messageReceivedEvent);

    verify(messageSender, times(ONCE))
        .sendError(messageChannel, exceptionMessage);
    verifyNoMoreInteractions(botCommand);
  }

  @Test
  @DisplayName("Unknown command produces an exception that sends as error message")
  void onMessageReceivedWithUnknownCommand() {
    final String testContent = "!unknown";
    when(message.getContentRaw())
        .thenReturn(testContent);

    final CommandDto commandDto = mock(CommandDto.class);
    final BotCommand botCommand = mock(BotCommand.class);

    when(tokenizer.toCommand(testContent))
        .thenReturn(commandDto);
    final String exceptionMessage = "A test exception message";
    when(commandService.findCommand(commandDto))
        .thenThrow(new CommandNotFoundException(exceptionMessage));

    listener.onMessageReceived(messageReceivedEvent);

    verify(messageSender, times(ONCE))
        .sendError(messageChannel, exceptionMessage);
    verifyNoMoreInteractions(botCommand, commandDto, commandService);
  }

  @Test
  @DisplayName("Unknown exception produces a sender call with error message")
  void onMessageReceivedWithUnknownException() {
    final String testContent = "!does not matter";
    when(message.getContentRaw())
        .thenReturn(testContent);

    final String aMessage = "Unknown exception's message";
    when(tokenizer.toCommand(testContent))
        .thenThrow(new RuntimeException(aMessage));

    listener.onMessageReceived(messageReceivedEvent);

    final String expectedMessage = "An unknown error occurred on running command: " + testContent;
    verify(messageSender, times(ONCE))
        .sendError(messageChannel, expectedMessage);
    verifyNoMoreInteractions(commandService);
  }
}