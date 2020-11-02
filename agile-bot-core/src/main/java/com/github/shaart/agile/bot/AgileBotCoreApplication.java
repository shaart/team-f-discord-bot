package com.github.shaart.agile.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConfigurationPropertiesScan("com.github.shaart.agile.bot.properties")
public class AgileBotCoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(AgileBotCoreApplication.class, args);
  }
}
