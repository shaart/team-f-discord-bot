package com.github.shaart.team.f.discord.bot;

import com.github.shaart.team.f.discord.bot.service.DiscordBot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConfigurationPropertiesScan("com.github.shaart.team.f.discord.bot.properties")
public class TeamFDiscordBotApplication implements CommandLineRunner {

  private final DiscordBot discordBot;

  public static void main(String[] args) {
    SpringApplication.run(TeamFDiscordBotApplication.class, args);
  }

  @Override
  public void run(String... args) {
    discordBot.start();
  }
}
