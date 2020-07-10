package com.github.shaart.team.f.discord.bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface BotCommand {

  int minArgumentsCount();

  String getAlias();

  String getDescription();

  String getUsage();

  void run(MessageReceivedEvent event, String... args);
}
