package com.github.shaart.agile.bot.discord.adapter.mapper.impl;

import com.github.shaart.agile.bot.discord.adapter.dto.MessageDto;
import com.github.shaart.agile.bot.discord.adapter.mapper.DtoMapper;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper implements DtoMapper<Message, MessageDto> {

  @Override
  public MessageDto toInternalDto(Message message) {
    return MessageDto.of(message);
  }
}
