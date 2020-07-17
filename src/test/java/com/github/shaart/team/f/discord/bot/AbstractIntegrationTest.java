package com.github.shaart.team.f.discord.bot;

import static org.mockito.Mockito.when;

import com.github.shaart.team.f.discord.bot.component.MessageSender;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles(profiles = "test")
public abstract class AbstractIntegrationTest {

  @MockBean
  protected MessageSender messageSender;

  @MockBean
  protected JDA jda;

  @Mock
  protected MessageReceivedEvent messageReceivedEvent;

  @Mock
  protected User testUser;

  @Mock
  protected MessageChannel messageChannel;

  @Mock
  protected Message message;

  @BeforeEach
  public void init() {
    when(messageReceivedEvent.getChannel())
        .thenReturn(messageChannel);
    when(messageReceivedEvent.getMessage())
        .thenReturn(message);
    when(messageReceivedEvent.getAuthor())
        .thenAnswer(invocation -> message.getAuthor());

    when(testUser.isBot())
        .thenReturn(Boolean.FALSE);
    when(testUser.getName())
        .thenReturn("Test user");
  }
}
