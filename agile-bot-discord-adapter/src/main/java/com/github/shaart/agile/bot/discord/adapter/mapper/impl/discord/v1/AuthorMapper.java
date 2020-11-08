package com.github.shaart.agile.bot.discord.adapter.mapper.impl.discord.v1;

import com.github.shaart.agile.bot.discord.adapter.dto.v1.AuthorDto;
import com.github.shaart.agile.bot.discord.adapter.mapper.DtoMapper;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper implements DtoMapper<User, AuthorDto> {

  @Override
  public AuthorDto toInternalModel(User author) {
    return AuthorDto.builder()
        .isBot(author.isBot())
        .build();
  }
}
