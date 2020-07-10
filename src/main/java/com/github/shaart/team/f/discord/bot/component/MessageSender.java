package com.github.shaart.team.f.discord.bot.component;

import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * Message sender for messages publishing in discord channel.
 */
public interface MessageSender {

  /**
   * Send a <b>common</b> message to channel.
   *
   * @param channel destination channel
   * @param message message to post
   */
  void sendMessage(MessageChannel channel, String message);

  /**
   * Send an <b>error</b> message to channel.
   *
   * @param channel destination channel
   * @param message message to post
   */
  void sendError(MessageChannel channel, String message);
}
