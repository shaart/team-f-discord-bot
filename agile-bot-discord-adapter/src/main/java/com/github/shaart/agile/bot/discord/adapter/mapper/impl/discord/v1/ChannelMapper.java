package com.github.shaart.agile.bot.discord.adapter.mapper.impl.discord.v1;

import com.github.shaart.agile.bot.discord.adapter.dto.v1.ChannelDto;
import com.github.shaart.agile.bot.discord.adapter.mapper.DtoMapper;
import com.github.shaart.agile.bot.util.exception.ExceptionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelMapper implements DtoMapper<MessageChannel, ChannelDto> {

  public ChannelDto toInternalDto(MessageChannel channel, long guildId) {
    return ChannelDto.builder()
        .id(channel.getId())
        .longId(channel.getIdLong())
        .name(channel.getName())
        .guildId(guildId)
        .build();
  }

  @Override
  public ChannelDto toInternalModel(MessageChannel receivedEvent) {
    throw ExceptionFactory.newNotImplementedException();
  }
}
