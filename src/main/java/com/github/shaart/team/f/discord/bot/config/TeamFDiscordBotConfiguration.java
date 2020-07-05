package com.github.shaart.team.f.discord.bot.config;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.shaart.team.f.discord.bot.properties.TeamFDiscordBotProperties;

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
