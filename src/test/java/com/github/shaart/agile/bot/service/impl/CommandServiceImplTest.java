package com.github.shaart.agile.bot.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.shaart.agile.bot.command.BotCommand;
import com.github.shaart.agile.bot.command.impl.HelpCommand;
import com.github.shaart.agile.bot.command.impl.PingCommand;
import com.github.shaart.agile.bot.command.impl.RandomCommand;
import com.github.shaart.agile.bot.component.MessageSender;
import com.github.shaart.agile.bot.dto.CommandDto;
import com.github.shaart.agile.bot.exception.CommandNotFoundException;
import com.github.shaart.agile.bot.exception.CommandValidationException;
import com.github.shaart.agile.bot.properties.AgileBotDiscordProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class CommandServiceImplTest {

  public static final String[] EMPTY_ARGUMENTS = new String[0];

  @Mock
  private AgileBotDiscordProperties properties;

  @Mock
  private MessageSender messageSender;

  private CommandServiceImpl commandService;

  @BeforeEach
  void setUp() {
    when(properties.getCommandPrefix())
        .thenReturn("!");

    final HashMap<String, BotCommand> commands = new HashMap<>();
    commands.put("help", new HelpCommand(properties, messageSender));
    commands.put("ping", new PingCommand(properties, messageSender));
    commands.put("random", new RandomCommand(properties, messageSender));

    commandService = new CommandServiceImpl(commands, properties);
  }

  @Test
  @DisplayName("Find an existing '!ping' command")
  void findCommandPing() {
    final CommandDto commandDto = CommandDto.builder()
        .command("!ping")
        .arguments(EMPTY_ARGUMENTS)
        .build();

    final BotCommand command = commandService.findCommand(commandDto);

    assertEquals(PingCommand.class, command.getClass());
  }

  @Test
  @DisplayName("Find an existing '!help' command")
  void findCommandHelp() {
    final CommandDto commandDto = CommandDto.builder()
        .command("!help")
        .arguments(EMPTY_ARGUMENTS)
        .build();

    final BotCommand command = commandService.findCommand(commandDto);

    assertEquals(HelpCommand.class, command.getClass());
  }

  @Test
  @DisplayName("Find an unknown command produces exception")
  void findCommandUnknown() {
    final CommandDto commandDto = CommandDto.builder()
        .command("!unknown")
        .arguments(EMPTY_ARGUMENTS)
        .build();

    final CommandNotFoundException exception = assertThrows(
        CommandNotFoundException.class, () -> commandService.findCommand(commandDto));

    final String message = exception.getMessage();
    assertEquals("Unknown command: !unknown", message);
  }

  @Test
  @DisplayName("Validate arguments for '!random' command without arguments produces exception")
  void validateArgumentsRandomWithoutArgs() {
    final RandomCommand randomCommand = new RandomCommand(properties, messageSender);
    final String[] emptyArgs = new String[0];

    final Executable executable = () -> commandService.validateArguments(randomCommand, emptyArgs);

    final CommandValidationException exception =
        assertThrows(CommandValidationException.class, executable);

    final String message = exception.getMessage();
    final String expected =
        "Incorrect arguments count. Expected at least " + randomCommand.minArgumentsCount()
            + "." + System.lineSeparator()
            + "Usage: " + randomCommand.getUsage();
    assertEquals(expected, message);
  }

  @Test
  @DisplayName("Validate arguments for '!random' command with arguments results no error")
  void validateArgumentsRandomWithArgs() {
    final RandomCommand randomCommand = new RandomCommand(properties, messageSender);
    final String[] args = {"1", "5"};

    final Executable executable = () -> commandService.validateArguments(randomCommand, args);

    assertDoesNotThrow(executable);
  }

  @Test
  @DisplayName("Get commands page without arguments got first 10 commands")
  void getCommandsPage() {
    final int defaultSize = 10;
    final int testCommandsCount = 15;
    final HashMap<String, BotCommand> commands = new HashMap<>(testCommandsCount);
    List<BotCommand> expected = new ArrayList<>(defaultSize);

    final int startIndex = 0;
    final int endIndex = startIndex + defaultSize;
    initCollections(testCommandsCount, startIndex, endIndex, commands, expected);

    commandService = new CommandServiceImpl(commands, properties);
    final List<BotCommand> commandsPage = commandService.getCommandsPage();

    assertElements(defaultSize, expected, commandsPage);
  }

  @Test
  @DisplayName("Get first commands page got first 10 commands as no arguments")
  void getCommandsPageFirst() {
    final int pageNumber = 1;
    final int defaultSize = 10;
    final int testCommandsCount = 15;
    final HashMap<String, BotCommand> commands = new HashMap<>(testCommandsCount);
    List<BotCommand> expected = new ArrayList<>(defaultSize);

    final int startIndex = 0;
    final int endIndex = startIndex + defaultSize;
    initCollections(testCommandsCount, startIndex, endIndex, commands, expected);

    commandService = new CommandServiceImpl(commands, properties);
    final List<BotCommand> commandsPage = commandService.getCommandsPage(pageNumber);

    assertElements(defaultSize, expected, commandsPage);
  }

  @Test
  @DisplayName("Get second commands page got last 7 commands of 17 commands total")
  void getCommandsPageSecond() {
    final int pageNumber = 2;
    final int defaultSize = 10;
    final int testCommandsCount = 17;
    final int shouldBeCommandsCount = 7;

    final HashMap<String, BotCommand> commands = new HashMap<>(testCommandsCount);
    List<BotCommand> expected = new ArrayList<>(defaultSize);

    final int startIndex = 10;
    final int endIndex = startIndex + defaultSize;
    initCollections(testCommandsCount, startIndex, endIndex, commands, expected);

    commandService = new CommandServiceImpl(commands, properties);
    final List<BotCommand> commandsPage = commandService.getCommandsPage(pageNumber);

    assertElements(shouldBeCommandsCount, expected, commandsPage);
  }

  @Test
  @DisplayName("Get second 5 commands page from 17 total")
  void getCommandsPageSecondFiveCommand() {
    final int pageNumber = 2;
    final int pageSize = 5;
    final int testCommandsCount = 17;
    final int shouldBeCommandsCount = 5;

    final HashMap<String, BotCommand> commands = new HashMap<>(testCommandsCount);
    List<BotCommand> expected = new ArrayList<>(pageSize);

    final int startIndex = 5;
    final int endIndex = startIndex + pageSize;
    initCollections(testCommandsCount, startIndex, endIndex, commands, expected);

    commandService = new CommandServiceImpl(commands, properties);
    final List<BotCommand> commandsPage = commandService.getCommandsPage(pageNumber, pageSize);

    assertElements(shouldBeCommandsCount, expected, commandsPage);
  }

  @Test
  @DisplayName("Get commands by default page number if it's negative")
  void getCommandsPageIncorrectPageNumber() {
    final int pageNumber = -1;
    final int pageSize = 5;
    final int testCommandsCount = 17;
    final int shouldBeCommandsCount = 5;

    final HashMap<String, BotCommand> commands = new HashMap<>(testCommandsCount);
    List<BotCommand> expected = new ArrayList<>(pageSize);

    final int startIndex = 0;
    final int endIndex = startIndex + pageSize;
    initCollections(testCommandsCount, startIndex, endIndex, commands, expected);

    commandService = new CommandServiceImpl(commands, properties);
    final List<BotCommand> commandsPage = commandService.getCommandsPage(pageNumber, pageSize);

    assertElements(shouldBeCommandsCount, expected, commandsPage);
  }

  @Test
  @DisplayName("Get commands by page size = 1 if it's negative")
  void getCommandsPageIncorrectPageSize() {
    final int pageNumber = 2;
    final int pageSize = -1;
    final int emergencyPageSize = 1;
    final int testCommandsCount = 17;

    final HashMap<String, BotCommand> commands = new HashMap<>(testCommandsCount);
    List<BotCommand> expected = new ArrayList<>(emergencyPageSize);

    final int startIndex = 1;
    final int endIndex = 1;
    initCollections(testCommandsCount, startIndex, endIndex, commands, expected);

    commandService = new CommandServiceImpl(commands, properties);
    final List<BotCommand> commandsPage = commandService.getCommandsPage(pageNumber, pageSize);

    assertElements(emergencyPageSize, expected, commandsPage);
  }

  @Test
  @DisplayName("Get firt command if page size and number are negative")
  void getCommandsPageIncorrectPageSizeAndPageNumber() {
    final int pageNumber = -1;
    final int pageSize = -1;
    final int emergencyPageSize = 1;
    final int testCommandsCount = 17;

    final HashMap<String, BotCommand> commands = new HashMap<>(testCommandsCount);
    List<BotCommand> expected = new ArrayList<>(emergencyPageSize);

    final int startIndex = 0;
    final int endIndex = 0;
    initCollections(testCommandsCount, startIndex, endIndex, commands, expected);

    commandService = new CommandServiceImpl(commands, properties);
    final List<BotCommand> commandsPage = commandService.getCommandsPage(pageNumber, pageSize);

    assertElements(emergencyPageSize, expected, commandsPage);
  }

  private void initCollections(int testCommandsCount, int startIndex, int endIndex,
      Map<String, BotCommand> commands, List<BotCommand> expected) {

    final boolean hasMoreThreeDigits = testCommandsCount >= 100;
    if (hasMoreThreeDigits) {
      throw new UnsupportedOperationException("Test: supported from 0 to 99 commands");
    }

    for (int i = 0; i < testCommandsCount; i++) {
      final boolean isFillingZeroNeeded = i < 10;
      String prefix = isFillingZeroNeeded ? "c0" : "c";
      final String commandAlias = prefix + i;

      final BotCommand command = mock(BotCommand.class);
      final String toStringCommand = String.format("Command with alias = '%s' and hash = %d",
          commandAlias, command.hashCode());
      lenient()
          .when(command.toString())
          .thenReturn(toStringCommand);

      commands.put(commandAlias, command);
      if (startIndex <= i && i <= endIndex) {
        expected.add(command);
      }
    }
  }

  private void assertElements(int shouldBeCommandsCount, List<BotCommand> expected,
      List<BotCommand> commandsPage) {

    assertEquals(shouldBeCommandsCount, commandsPage.size());

    for (int i = 0; i < shouldBeCommandsCount; i++) {
      assertSame(expected.get(i), commandsPage.get(i));
    }
  }
}