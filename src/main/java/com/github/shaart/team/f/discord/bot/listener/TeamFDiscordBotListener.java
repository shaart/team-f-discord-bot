package com.github.shaart.team.f.discord.bot.listener;

import com.github.shaart.team.f.discord.bot.command.BotCommand;
import com.github.shaart.team.f.discord.bot.component.MessageSender;
import com.github.shaart.team.f.discord.bot.component.Tokenizer;
import com.github.shaart.team.f.discord.bot.dto.CommandDto;
import com.github.shaart.team.f.discord.bot.dto.ChannelDto;
import com.github.shaart.team.f.discord.bot.dto.EventDto;
import com.github.shaart.team.f.discord.bot.dto.MessageDto;
import com.github.shaart.team.f.discord.bot.exception.CommandNotFoundException;
import com.github.shaart.team.f.discord.bot.exception.CommandValidationException;
import com.github.shaart.team.f.discord.bot.mapper.impl.EventMapper;
import com.github.shaart.team.f.discord.bot.properties.TeamFDiscordBotProperties;
import com.github.shaart.team.f.discord.bot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamFDiscordBotListener extends ListenerAdapter {

  public static final String NO_MESSAGE = "";

  private final Tokenizer tokenizer;
  private final MessageSender messageSender;
  private final CommandService commandService;
  private final TeamFDiscordBotProperties properties;
  private final EventMapper eventMapper;

  /**
   * Logic for received messages in channels.
   * @param event an event
   */
  @Override
  public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
    try {
      handleEvent(event);
    } catch (Exception e) {
      log.error("An unexpected exception occurred", e);
    }
  }

  private void handleEvent(@Nonnull MessageReceivedEvent receivedEvent) {
    EventDto event = eventMapper.toInternalDto(receivedEvent);
    if (event.getAuthor().isBot()) {
      return;
    }
    MessageDto message = event.getMessage();
    String content = event.getContent();
    if (!content.startsWith(properties.getCommandPrefix())) {
      return;
    }

    log.debug("Got a message from {}", message.getAuthorName());
    log.trace("The message is: '{}'", content);
    final ChannelDto channel = event.getChannel();
    try {
      final CommandDto commandDto = tokenizer.toCommand(content);
      final BotCommand botCommand = commandService.findCommand(commandDto);
      final String[] args = commandDto.getArguments();

      commandService.validateArguments(botCommand, args);

      botCommand.run(event, args);
    } catch (CommandValidationException | CommandNotFoundException e) {
      log.error(NO_MESSAGE, e);
      messageSender.sendError(channel, e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      final String errorMessage = "An unknown error occurred on running command: " + content;
      messageSender.sendError(channel, errorMessage);
    }
  }
}
