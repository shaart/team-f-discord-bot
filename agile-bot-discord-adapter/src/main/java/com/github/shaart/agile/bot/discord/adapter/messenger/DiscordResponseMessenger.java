package com.github.shaart.agile.bot.discord.adapter.messenger;

import com.github.shaart.agile.bot.discord.adapter.dto.v1.EventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordResponseMessenger {

  private static final String DISCORD_EVENT_INFO_KEY = "d";

  private final JDA jda;

  public void sendResponse(EventDto event) {
    String discordEventJsonContext = event.getEventJsonContext();
    DataObject eventData = DataObject.fromJson(discordEventJsonContext);
    DataObject eventInfo = eventData.getObject(DISCORD_EVENT_INFO_KEY);

    String channelId = eventInfo.getString("channel_id");
    TextChannel messageChannel = jda.getTextChannelById(channelId);

    Objects.requireNonNull(messageChannel,
        "Message channel with id = " + channelId + " is not found");

    String content = event.getMessage().getContent();
    messageChannel.sendMessage(content).queue();
  }
}
