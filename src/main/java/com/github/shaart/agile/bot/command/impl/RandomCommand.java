package com.github.shaart.agile.bot.command.impl;

import static net.logstash.logback.marker.Markers.append;

import com.github.shaart.agile.bot.dto.ChannelDto;
import com.github.shaart.agile.bot.dto.EventDto;
import com.github.shaart.agile.bot.command.AbstractBotCommand;
import com.github.shaart.agile.bot.component.MessageSender;
import com.github.shaart.agile.bot.properties.AgileBotDiscordProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component(RandomCommand.RANDOM_COMMAND_ALIAS)
public class RandomCommand extends AbstractBotCommand {

  public static final String RANDOM_COMMAND_ALIAS = "random";

  @Autowired
  public RandomCommand(AgileBotDiscordProperties properties, MessageSender messageSender) {
    super(properties, messageSender);
  }

  @Override
  public int minArgumentsCount() {
    return 2;
  }

  @Override
  public String getAlias() {
    return RANDOM_COMMAND_ALIAS;
  }

  @Override
  public String getDescription() {
    return "Get a random number in (min,max)";
  }

  @Override
  public String getUsage() {
    return ""
        + "```"
        + commandPrefix + RANDOM_COMMAND_ALIAS + " <min> <max>\n"
        + "where: min - minimal number, inclusive\n"
        + "       max - maximal number, exclusive\n"
        + "```";
  }

  @Override
  public void run(EventDto event, String... args) {
    final ChannelDto channel = event.getChannel();

    final int min = Integer.parseInt(args[0]);
    final int max = Integer.parseInt(args[1]);
    log.trace(append("min_number", min)
            .and(append("max_number", max)),
        "Min and max borders of random number is");

    final int randomNumber = ThreadLocalRandom.current().nextInt(min, max);

    log.debug(append("random_number", randomNumber), "Result random number is");

    messageSender.sendMessage(channel, "`" + randomNumber + "`");
  }
}
