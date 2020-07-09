package com.github.shaart.team.f.discord.bot.component;

import com.github.shaart.team.f.discord.bot.dto.CommandDto;

public interface Tokenizer {

  CommandDto toCommand(String message);
}
