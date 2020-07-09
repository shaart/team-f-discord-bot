package com.github.shaart.team.f.discord.bot.config;

import com.github.shaart.team.f.discord.bot.properties.TeamFDiscordBotProperties;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class TeamFDiscordBotConfiguration {

  @Bean
  public TeamFDiscordBotProperties teamFDiscordBotProperties() {
    return new TeamFDiscordBotProperties();
  }

  @Bean
  public JDABuilder jdaBuilder(TeamFDiscordBotProperties teamFDiscordBotProperties) {
    return JDABuilder.create(teamFDiscordBotProperties.getToken(), Collections.emptyList());
  }
}
