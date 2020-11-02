package com.github.shaart.agile.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

  private boolean isBot;

  /**
   * Creates a DTO from library's DTO.
   *
   * @param realAuthor library's DTO
   * @return internal DTO
   */
  public static AuthorDto of(User realAuthor) {
    return AuthorDto.builder()
        .isBot(realAuthor.isBot())
        .build();
  }
}
