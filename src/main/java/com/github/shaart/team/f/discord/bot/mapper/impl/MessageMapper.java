package com.github.shaart.team.f.discord.bot.mapper.impl;

import com.github.shaart.team.f.discord.bot.dto.MessageDto;
import com.github.shaart.team.f.discord.bot.mapper.DtoMapper;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper implements DtoMapper<Message, MessageDto> {

  @Override
  public MessageDto toInternalDto(Message message) {
    return MessageDto.of(message);
  }
}
