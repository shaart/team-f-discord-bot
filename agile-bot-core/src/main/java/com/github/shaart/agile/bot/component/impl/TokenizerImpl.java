package com.github.shaart.agile.bot.component.impl;

import com.github.shaart.agile.bot.dto.CommandDto;
import com.github.shaart.agile.bot.component.Tokenizer;
import com.github.shaart.agile.bot.exception.CommandValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TokenizerImpl implements Tokenizer {

  public static final int COMMAND_INDEX = 0;
  public static final int FIRST_ARGUMENT_INDEX = 1;
  public static final char QUOTE_SYMBOL = '"';
  public static final char SPACE_SYMBOL = ' ';
  public static final int EMPTY_LENGTH = 0;

  @Override
  public CommandDto toCommand(String message) {
    final String[] tokens = splitTokens(message);

    if (tokens.length == EMPTY_LENGTH) {
      throw new CommandValidationException("Can't process a command without command alias");
    }

    final String command = tokens[COMMAND_INDEX];
    final String[] arguments = Arrays.copyOfRange(tokens, FIRST_ARGUMENT_INDEX, tokens.length);
    return CommandDto.builder()
        .command(command)
        .arguments(arguments)
        .build();
  }

  private String[] splitTokens(String initial) {
    List<String> tokens = new ArrayList<>();
    StringBuilder builder = new StringBuilder();

    boolean insideQuote = false;
    for (char character : initial.toCharArray()) {
      if (character == QUOTE_SYMBOL) {
        insideQuote = !insideQuote;
        continue;
      }

      final boolean isSpaceOutsideQuote = character == SPACE_SYMBOL && !insideQuote;
      if (isSpaceOutsideQuote) {
        final String aToken = builder.toString();
        tokens.add(aToken);
        builder.setLength(EMPTY_LENGTH);
      } else {
        builder.append(character);
      }
    }
    final String lastToken = builder.toString();
    tokens.add(lastToken);

    return tokens.toArray(new String[0]);
  }
}
