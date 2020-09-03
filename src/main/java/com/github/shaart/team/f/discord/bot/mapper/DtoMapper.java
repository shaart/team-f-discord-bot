package com.github.shaart.team.f.discord.bot.mapper;

/**
 * Mapper interface for DTOs.
 *
 * @param <S> source
 * @param <T> target
 */
public interface DtoMapper<S, T> {

  /**
   * Convertes an event to internal event DTO.
   *
   * @param receivedEvent event of library type
   * @return event of app's type
   */
  T toInternalDto(S receivedEvent);
}
