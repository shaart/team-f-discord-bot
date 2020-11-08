package com.github.shaart.agile.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

  private ChannelDto channel;
  private AuthorDto author;
  private MessageDto message;
  private String serverName;
  private String serverId;

  public String getContent() {
    return message.getContent();
  }
}
