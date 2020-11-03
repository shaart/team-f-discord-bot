package com.github.shaart.agile.bot.discord.adapter.mapper.impl;

import com.github.shaart.agile.bot.discord.adapter.dto.AuthorDto;
import com.github.shaart.agile.bot.discord.adapter.dto.ChannelDto;
import com.github.shaart.agile.bot.discord.adapter.dto.MessageDto;
import com.github.shaart.agile.bot.discord.adapter.mapper.DtoMapper;
import com.github.shaart.agile.bot.discord.adapter.dto.EventDto;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Setter(onMethod = @__(@Autowired))
public class EventMapper implements DtoMapper<MessageReceivedEvent, EventDto> {

  private ChannelMapper channelMapper;
  private AuthorMapper authorMapper;
  private MessageMapper messageMapper;

  @Override
  public EventDto toInternalDto(MessageReceivedEvent receivedEvent) {
    final MessageChannel channel = receivedEvent.getChannel();
    final ChannelDto channelDto = channelMapper.toInternalDto(channel);

    final Message message = receivedEvent.getMessage();
    final MessageDto messageDto = messageMapper.toInternalDto(message);

    final User author = message.getAuthor();
    final AuthorDto authorDto = authorMapper.toInternalDto(author);

    return EventDto.builder()
        .channel(channelDto)
        .author(authorDto)
        .message(messageDto)
        .realEvent(receivedEvent)
        .build();
  }
}
