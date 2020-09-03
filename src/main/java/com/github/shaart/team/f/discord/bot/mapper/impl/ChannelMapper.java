package com.github.shaart.team.f.discord.bot.mapper.impl;

import com.github.shaart.team.f.discord.bot.dto.ChannelDto;
import com.github.shaart.team.f.discord.bot.mapper.DtoMapper;
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
