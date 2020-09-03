package com.github.shaart.team.f.discord.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

  private MessageReceivedEvent realEvent;

  private ChannelDto channel;
  private AuthorDto author;
  private MessageDto message;

  public String getContent() {
    return message.getContent();
  }
}
