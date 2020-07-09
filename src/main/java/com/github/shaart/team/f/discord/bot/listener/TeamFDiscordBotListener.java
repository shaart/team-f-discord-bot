package com.github.shaart.team.f.discord.bot.listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class TeamFDiscordBotListener extends ListenerAdapter {

  @Override
  public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) {
      return;
    }

    Message message = event.getMessage();
    String content = message.getContentRaw();
    if (content.equals("!ping")) {
      MessageChannel channel = event.getChannel();
      channel.sendMessage("Pong!").queue();
    }
  }
}
