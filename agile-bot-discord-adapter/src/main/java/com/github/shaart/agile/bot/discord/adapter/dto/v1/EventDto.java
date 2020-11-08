package com.github.shaart.agile.bot.discord.adapter.dto.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.shaart.agile.bot.discord.adapter.enums.EventSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

  @JsonProperty("server_id")
  private String serverId;

  @JsonProperty("server_name")
  private String serverName;

  @JsonProperty("channel")
  private ChannelDto channel;

  @JsonProperty("author")
  private AuthorDto author;

  @JsonProperty("message")
  private MessageDto message;

  @JsonProperty("event_json_context")
  private String eventJsonContext;

  @JsonProperty("source_type")
  private EventSource sourceType;

  public String getContent() {
    return message.getContent();
  }
}
