package com.github.shaart.agile.bot.discord.adapter.mapper.impl.discord.v1;

import com.github.shaart.agile.bot.discord.adapter.dto.v1.MessageDto;
import com.github.shaart.agile.bot.discord.adapter.mapper.DtoMapper;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper implements DtoMapper<Message, MessageDto> {

  @Override
  public MessageDto toInternalModel(Message message) {
    return MessageDto.builder()
        .authorName(message.getAuthor().getName())
        .content(message.getContentRaw())
        .build();
  }
}
