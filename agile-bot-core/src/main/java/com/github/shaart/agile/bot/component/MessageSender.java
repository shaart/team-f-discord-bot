package com.github.shaart.agile.bot.component;

import com.github.shaart.agile.bot.dto.ChannelDto;

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
  void sendMessage(ChannelDto channel, String message);

  /**
   * Send an <b>error</b> message to channel.
   *
   * @param channel destination channel
   * @param message message to post
   */
  void sendError(ChannelDto channel, String message);
}
