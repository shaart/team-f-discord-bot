package com.github.shaart.agile.bot.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ChannelDto {

  public void sendMessage(String resultMessage) {
    throw new UnsupportedOperationException("sendMessage is not supported");
  }

  public String getName() {
    throw new UnsupportedOperationException("getName is not supported");
  }
}
