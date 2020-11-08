package com.github.shaart.agile.bot.discord.adapter.mapper.impl;

import com.github.shaart.agile.bot.discord.adapter.mapper.DtoMapper;
import com.github.shaart.agile.bot.discord.adapter.dto.AuthorDto;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper implements DtoMapper<User, AuthorDto> {

  @Override
  public AuthorDto toInternalDto(User author) {
    return AuthorDto.of(author);
  }
}
