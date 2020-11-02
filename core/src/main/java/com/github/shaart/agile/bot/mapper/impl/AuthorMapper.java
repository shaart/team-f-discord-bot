package com.github.shaart.agile.bot.mapper.impl;

import com.github.shaart.agile.bot.dto.AuthorDto;
import com.github.shaart.agile.bot.mapper.DtoMapper;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper implements DtoMapper<User, AuthorDto> {

  @Override
  public AuthorDto toInternalDto(User author) {
    return AuthorDto.of(author);
  }
}
