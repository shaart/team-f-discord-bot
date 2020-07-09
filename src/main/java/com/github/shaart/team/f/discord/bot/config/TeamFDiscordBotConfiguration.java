package com.github.shaart.team.f.discord.bot.config;

import com.github.shaart.team.f.discord.bot.listener.TeamFDiscordBotListener;
import com.github.shaart.team.f.discord.bot.properties.TeamFDiscordBotProperties;
import com.github.shaart.team.f.discord.bot.util.BotStringUtils;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

import javax.security.auth.login.LoginException;

@Configuration
@RequiredArgsConstructor
public class TeamFDiscordBotConfiguration {

  public static final List<GatewayIntent> GATEWAY_INTENTS = Collections
      .singletonList(GatewayIntent.GUILD_MESSAGES);

  @Bean
  public TeamFDiscordBotListener teamFDiscordBotListener() {
    return new TeamFDiscordBotListener();
  }

  /**
   * Java Discord Application bean.
   *
   * @param properties application's properties
   * @param listener   message listener
   * @return built jda
   * @throws LoginException if there is a wrong token specified
   */
  @Bean
  public JDA jda(TeamFDiscordBotProperties properties,
      TeamFDiscordBotListener listener) throws LoginException {

    final String botToken = properties.getToken();
    if (BotStringUtils.isEmpty(botToken)) {
      throw new IllegalStateException("Bot's token must be specified");
    }

    return JDABuilder.create(botToken, GATEWAY_INTENTS)
        .setStatus(OnlineStatus.ONLINE)
        .addEventListeners(listener)
        .build();
  }
}
