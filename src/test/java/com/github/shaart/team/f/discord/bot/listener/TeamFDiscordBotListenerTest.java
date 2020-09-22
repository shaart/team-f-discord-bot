package com.github.shaart.team.f.discord.bot.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.read.ListAppender;
import com.github.shaart.team.f.discord.bot.command.BotCommand;
import com.github.shaart.team.f.discord.bot.component.MessageSender;
import com.github.shaart.team.f.discord.bot.component.Tokenizer;
import com.github.shaart.team.f.discord.bot.dto.ChannelDto;
import com.github.shaart.team.f.discord.bot.dto.CommandDto;
import com.github.shaart.team.f.discord.bot.dto.EventDto;
import com.github.shaart.team.f.discord.bot.exception.CommandNotFoundException;
import com.github.shaart.team.f.discord.bot.exception.CommandValidationException;
import com.github.shaart.team.f.discord.bot.mapper.impl.AuthorMapper;
import com.github.shaart.team.f.discord.bot.mapper.impl.ChannelMapper;
import com.github.shaart.team.f.discord.bot.mapper.impl.EventMapper;
import com.github.shaart.team.f.discord.bot.mapper.impl.MessageMapper;
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
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class TeamFDiscordBotListenerTest {

  private static final int ONCE = 1;
  private static final String TEST_CHANNEL_NAME = "test-channel-name";

  @Mock
  private User testUser;

  @Mock
  private MessageChannel realMessageChannel;

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

  @Spy
  private EventMapper eventMapper;

  @InjectMocks
  private TeamFDiscordBotListener listener;

  @BeforeEach
  void setUp() {
    lenient()
        .when(messageReceivedEvent.getChannel())
        .thenReturn(realMessageChannel);
    lenient()
        .when(realMessageChannel.getName())
        .thenReturn(TEST_CHANNEL_NAME);

    lenient()
        .when(messageReceivedEvent.getMessage())
        .thenReturn(message);

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

    eventMapper.setAuthorMapper(new AuthorMapper());
    eventMapper.setChannelMapper(new ChannelMapper());
    eventMapper.setMessageMapper(new MessageMapper());
//    when(eventMapper.toInternalDto(messageReceivedEvent))
//        .thenCallRealMethod();
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

    ArgumentMatcher<ChannelDto> hasRealChannel =
        channelDto -> Objects.equals(channelDto.getName(), realMessageChannel.getName());
    verify(messageSender, times(ONCE))
        .sendError(argThat(hasRealChannel), eq(exceptionMessage));
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

    ArgumentMatcher<ChannelDto> hasRealChannel =
        channelDto -> Objects.equals(channelDto.getName(), realMessageChannel.getName());
    verify(messageSender, times(ONCE))
        .sendError(argThat(hasRealChannel), eq(exceptionMessage));
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
        .sendError(any(ChannelDto.class), eq(expectedMessage));
    verifyNoMoreInteractions(commandService);
  }

  @Test
  @DisplayName("Correctly received command has run")
  void onMessageReceivedRunFoundCommand() {
    final String testContent = "!random 1 6";
    when(message.getContentRaw())
        .thenReturn(testContent);

    final CommandDto commandDto = mock(CommandDto.class);
    final BotCommand botCommand = mock(BotCommand.class);

    when(tokenizer.toCommand(testContent))
        .thenReturn(commandDto);
    when(commandService.findCommand(commandDto))
        .thenReturn(botCommand);

    final String[] testArguments = new String[]{"1", "6"};
    when(commandDto.getArguments())
        .thenReturn(testArguments);
    doNothing()
        .when(commandService).validateArguments(botCommand, testArguments);
    doNothing()
        .when(botCommand).run(any(EventDto.class), eq(testArguments));

    listener.onMessageReceived(messageReceivedEvent);

    final ArgumentCaptor<String> argsCaptor = ArgumentCaptor.forClass(String.class);
    verify(botCommand, times(ONCE))
        .run(any(EventDto.class), argsCaptor.capture());
    assertEquals(Arrays.asList(testArguments), argsCaptor.getAllValues());
    verifyNoMoreInteractions(botCommand);
  }

  @Test
  @DisplayName("Handle unexpected sender error correctly with log")
  void onMessageReceivedWithUnexpectedErrorOnRun() {
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

    doThrow(RuntimeException.class)
        .when(commandService).validateArguments(botCommand, testArguments);
    final RuntimeException causeException = new RuntimeException("a cause");
    final RuntimeException expectedException =
        new RuntimeException("A test exception message", causeException);
    doThrow(expectedException)
        .when(messageSender).sendError(any(ChannelDto.class), anyString());

    final Logger logger = (Logger) LoggerFactory.getLogger(TeamFDiscordBotListener.class);
    final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    logger.addAppender(listAppender);

    listener.onMessageReceived(messageReceivedEvent);

    final List<ILoggingEvent> eventList = listAppender.list;
    final int lastIndex = eventList.size() - 1;
    final ILoggingEvent lastEvent = eventList.get(lastIndex);
    assertEquals(Level.ERROR, lastEvent.getLevel());
    assertEquals("An unexpected exception occurred", lastEvent.getMessage());

    final IThrowableProxy throwableProxy = lastEvent.getThrowableProxy();
    assertEquals(expectedException.getClass().getName(), throwableProxy.getClassName());
    assertEquals(expectedException.getMessage(), throwableProxy.getMessage());
    assertEquals(causeException.getMessage(), throwableProxy.getCause().getMessage());
  }
}