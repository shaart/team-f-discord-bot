package com.github.shaart.agile.bot.component;

import com.github.shaart.agile.bot.dto.CommandDto;
import com.github.shaart.agile.bot.exception.CommandValidationException;

public interface Tokenizer {

  /**
   * Parses initial message into command and its tokens (arguments).
   *
   * @param message initial message
   * @return DTO with command and arguments
   * @throws CommandValidationException if no tokens in the string
   */
  CommandDto toCommand(String message);
}
