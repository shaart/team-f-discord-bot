package com.github.shaart.agile.bot.discord.adapter.mapper.impl;

import com.github.shaart.agile.bot.discord.adapter.mapper.DtoMapper;
import com.github.shaart.agile.bot.discord.adapter.dto.ChannelDto;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class ChannelMapper implements DtoMapper<MessageChannel, ChannelDto> {

  @Override
  public ChannelDto toInternalDto(MessageChannel channel) {
    return ChannelDto.builder()
        .realChannel(channel)
        .build();
  }
}
