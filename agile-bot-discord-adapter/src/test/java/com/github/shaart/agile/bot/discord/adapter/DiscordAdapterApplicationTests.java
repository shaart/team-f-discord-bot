package com.github.shaart.agile.bot.discord.adapter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import net.dv8tion.jda.api.JDA;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class DiscordAdapterApplicationTests {

  @MockBean
  private JDA jda;

  @Test
  void contextLoads() {
    assertNotNull(jda);
  }

}
