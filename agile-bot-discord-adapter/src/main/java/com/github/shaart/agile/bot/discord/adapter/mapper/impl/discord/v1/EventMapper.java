package com.github.shaart.agile.bot.discord.adapter.mapper.impl.discord.v1;

import com.github.shaart.agile.bot.discord.adapter.context.ThreadLocalContext;
import com.github.shaart.agile.bot.discord.adapter.dto.v1.AuthorDto;
import com.github.shaart.agile.bot.discord.adapter.dto.v1.ChannelDto;
import com.github.shaart.agile.bot.discord.adapter.dto.v1.EventDto;
import com.github.shaart.agile.bot.discord.adapter.dto.v1.MessageDto;
import com.github.shaart.agile.bot.discord.adapter.enums.EventSource;
import com.github.shaart.agile.bot.discord.adapter.mapper.DtoMapper;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Setter(onMethod = @__(@Autowired))
public class EventMapper implements DtoMapper<MessageReceivedEvent, EventDto> {

  private ChannelMapper channelMapper;
  private AuthorMapper authorMapper;
  private MessageMapper messageMapper;
  private ThreadLocalContext threadLocalContext;

  @Override
  public EventDto toInternalModel(MessageReceivedEvent receivedEvent) {
    final MessageChannel channel = receivedEvent.getChannel();
    final ChannelDto channelDto = channelMapper.toInternalDto(channel,
        receivedEvent.getGuild().getIdLong());

    final Message message = receivedEvent.getMessage();
    final MessageDto messageDto = messageMapper.toInternalModel(message);

    final User author = message.getAuthor();
    final AuthorDto authorDto = authorMapper.toInternalModel(author);

    DataObject discordEventContext = threadLocalContext.getDiscordEventContext().get();

    return EventDto.builder()
        .channel(channelDto)
        .author(authorDto)
        .message(messageDto)
        .serverId(receivedEvent.getGuild().getId())
        .serverName(receivedEvent.getGuild().getName())
        .sourceType(EventSource.DISCORD)
        .eventJsonContext(discordEventContext.toString())
        .build();
  }
}
