package com.github.shaart.team.f.discord.bot.command.impl;

import static com.github.shaart.team.f.discord.bot.service.CommandService.DEFAULT_PAGE_NUMBER;
import static com.github.shaart.team.f.discord.bot.service.CommandService.DEFAULT_PAGE_SIZE;

import com.github.shaart.team.f.discord.bot.command.AbstractBotCommand;
import com.github.shaart.team.f.discord.bot.command.BotCommand;
import com.github.shaart.team.f.discord.bot.component.MessageSender;
import com.github.shaart.team.f.discord.bot.dto.CommandDto;
import com.github.shaart.team.f.discord.bot.properties.TeamFDiscordBotProperties;
import com.github.shaart.team.f.discord.bot.service.CommandService;
import lombok.Setter;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component(HelpCommand.HELP_COMMAND_ALIAS)
public class HelpCommand extends AbstractBotCommand {

  public static final String HELP_COMMAND_ALIAS = "help";
  private static final int NO_ARGUMENTS = 0;
  private static final int PAGE_SEARCH_ARGS_SIZE = 2;
  private static final int PAGE_NUMBER_INDEX = 0;
  private static final int PAGE_SIZE_INDEX = 1;
  private static final int SINGLE_ARGUMENT_INDEX = 0;
  private static final int SINGLE_ELEMENT = 1;

  @Setter(onMethod = @__(@Autowired))
  private CommandService commandService;

  @Autowired
  public HelpCommand(TeamFDiscordBotProperties properties, MessageSender messageSender) {
    super(properties, messageSender);
  }

  @Override
  public int minArgumentsCount() {
    return 0;
  }

  @Override
  public String getAlias() {
    return HELP_COMMAND_ALIAS;
  }

  @Override
  public String getUsage() {
    return ""
        + "```"
        + commandPrefix + HELP_COMMAND_ALIAS + " <command name>\n"
        + "or\n"
        + commandPrefix + HELP_COMMAND_ALIAS + " <page number> <page size>\n"
        + "where:\n"
        + "  <page number> - number of page per commands. Default: " + DEFAULT_PAGE_NUMBER + ".\n"
        + "  <page size> - number of commands to show. Default: " + DEFAULT_PAGE_SIZE + ".\n"
        + "\n"
        + "Examples:\n"
        + "help random - show usage of command 'random'\n"
        + "help 1 5 - show first 5 commands info"
        + "```";
  }

  @Override
  public String getDescription() {
    return "Commands help";
  }

  @Override
  public void run(MessageReceivedEvent event, String... args) {
    final MessageChannel channel = event.getChannel();

    List<BotCommand> commandsPage = getBotCommands(args);

    final String message;
    if (commandsPage.size() == SINGLE_ELEMENT) {
      message = "Usage:\n" + commandsPage.get(0).getUsage();
    } else {
      message = commandsPage.stream()
          .map(botCommand -> botCommand.getAlias() + " - " + botCommand.getDescription())
          .collect(Collectors.joining("\n", "```", "```"));
    }
    messageSender.sendMessage(channel, message);
  }

  private List<BotCommand> getBotCommands(String[] args) {
    if (args.length == NO_ARGUMENTS) {
      return commandService.getCommandsPage();
    }

    if (args.length >= PAGE_SEARCH_ARGS_SIZE) {
      final int pageNumber = Integer.parseInt(args[PAGE_NUMBER_INDEX]);
      final int pageSize = Integer.parseInt(args[PAGE_SIZE_INDEX]);
      return commandService.getCommandsPage(pageNumber, pageSize);
    }

    final String argument = args[SINGLE_ARGUMENT_INDEX];
    if (isNumber(argument)) {
      final int pageNumber = Integer.parseInt(argument);
      return commandService.getCommandsPage(pageNumber);
    } else {
      return findSingleCommand(argument);
    }
  }

  @NotNull
  private List<BotCommand> findSingleCommand(String alias) {
    final String commandWithPrefix = applicationProperties.getCommandPrefix() + alias;
    final CommandDto commandDto = CommandDto.forName(commandWithPrefix);
    final BotCommand command = commandService.findCommand(commandDto);
    return Collections.singletonList(command);
  }

  private boolean isNumber(String argument) {
    return argument.matches("\\d+");
  }
}
