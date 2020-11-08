package com.github.shaart.agile.bot.discord.adapter.dto.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

  @JsonProperty("author_name")
  private String authorName;

  @JsonProperty("content")
  private String content;
}
