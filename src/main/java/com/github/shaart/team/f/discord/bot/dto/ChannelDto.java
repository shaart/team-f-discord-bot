package com.github.shaart.team.f.discord.bot.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.MessageChannel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private MessageChannel realChannel;

  public void sendMessage(String resultMessage) {
    realChannel.sendMessage(resultMessage).queue();
  }

  public String getName() {
    return realChannel.getName();
  }
}
