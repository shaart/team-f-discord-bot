package com.github.shaart.team.f.discord.bot.component;

import net.dv8tion.jda.api.entities.MessageChannel;

public interface MessageSender {

  void sendMessage(MessageChannel channel, String message);

  void sendError(MessageChannel channel, String message);
}
