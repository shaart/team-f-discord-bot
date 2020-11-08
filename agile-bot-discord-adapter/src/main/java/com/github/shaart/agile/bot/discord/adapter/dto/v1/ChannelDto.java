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
public class ChannelDto {

  @JsonProperty("id")
  private String id;

  @JsonProperty("long_id")
  private Long longId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("type_id")
  private int typeId;

  @JsonProperty("guild_id")
  private long guildId;
}
