package com.github.shaart.team.f.discord.bot.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private MessageReceivedEvent realEvent;

  private ChannelDto channel;
  private AuthorDto author;
  private MessageDto message;

  public String getContent() {
    return message.getContent();
  }

  public String getServerName() {
    return realEvent.getGuild().getName();
  }

  public String getServerId() {
    return realEvent.getGuild().getId();
  }
}
