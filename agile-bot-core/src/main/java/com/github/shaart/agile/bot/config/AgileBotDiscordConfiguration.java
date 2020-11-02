package com.github.shaart.agile.bot.config;

import com.github.shaart.agile.bot.listener.AgileBotDiscordListener;
import com.github.shaart.agile.bot.util.BotStringUtils;
import com.github.shaart.agile.bot.properties.AgileBotDiscordProperties;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

import javax.security.auth.login.LoginException;

@Configuration
@RequiredArgsConstructor
public class AgileBotDiscordConfiguration {

  public static final List<GatewayIntent> GATEWAY_INTENTS = Collections
      .singletonList(GatewayIntent.GUILD_MESSAGES);

  /**
   * Java Discord Application bean.
   *
   * @param properties application's properties
   * @param listener   message listener
   * @return built jda
   * @throws LoginException if there is a wrong token specified
   */
  @Bean
  public JDA jda(AgileBotDiscordProperties properties,
      AgileBotDiscordListener listener) throws LoginException {

    final String botToken = properties.getToken();
    if (BotStringUtils.isEmpty(botToken)) {
      throw new IllegalStateException("Bot's token must be specified");
    }

    final String activityStatus = String.format("%shelp",
        properties.getCommandPrefix());
    return JDABuilder.create(botToken, GATEWAY_INTENTS)
        .setStatus(OnlineStatus.ONLINE)
        .setActivity(Activity.listening(activityStatus))
        .addEventListeners(listener)
        .build();
  }
}
