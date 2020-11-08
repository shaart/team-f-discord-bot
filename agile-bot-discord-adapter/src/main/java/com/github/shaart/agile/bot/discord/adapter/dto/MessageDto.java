package com.github.shaart.agile.bot.discord.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.Message;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

  private String authorName;
  private String content;

  /**
   * Creates a DTO from library's DTO.
   *
   * @param realMessage library's DTO
   * @return internal DTO
   */
  public static MessageDto of(Message realMessage) {
    return MessageDto.builder()
        .authorName(realMessage.getAuthor().getName())
        .content(realMessage.getContentRaw())
        .build();
  }
}
