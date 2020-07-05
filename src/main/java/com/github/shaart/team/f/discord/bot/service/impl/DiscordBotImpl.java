package com.github.shaart.team.f.discord.bot.service.impl;

import com.github.shaart.team.f.discord.bot.service.DiscordBot;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscordBotImpl implements DiscordBot {

  private final JDABuilder jdaBuilder;

  @Override
  public void start() {
    //TODO implement me
  }
}
