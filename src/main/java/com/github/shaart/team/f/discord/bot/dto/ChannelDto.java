package com.github.shaart.team.f.discord.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.MessageChannel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {

  private MessageChannel realChannel;

  public void sendMessage(String resultMessage) {
    realChannel.sendMessage(resultMessage).queue();
  }
}
