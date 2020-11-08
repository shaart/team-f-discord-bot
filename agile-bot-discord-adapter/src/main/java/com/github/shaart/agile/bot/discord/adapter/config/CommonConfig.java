package com.github.shaart.agile.bot.discord.adapter.config;

import com.github.shaart.agile.bot.discord.adapter.context.ThreadLocalContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

  @Bean
  public ThreadLocalContext threadLocalContext() {
    return ThreadLocalContext.getInstance();
  }
}
