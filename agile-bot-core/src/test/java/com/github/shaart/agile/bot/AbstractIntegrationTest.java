package com.github.shaart.agile.bot;

import static org.mockito.Mockito.when;

import com.github.shaart.agile.bot.component.MessageSender;
import com.github.shaart.agile.bot.dto.AuthorDto;
import com.github.shaart.agile.bot.dto.ChannelDto;
import com.github.shaart.agile.bot.dto.EventDto;
import com.github.shaart.agile.bot.dto.MessageDto;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
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
  protected MessageReceivedEvent realMessageReceivedEvent;

  @Mock
  protected EventDto messageReceivedEvent;
  @Mock
  protected ChannelDto channelDto;
  @Mock
  protected AuthorDto authorDto;
  @Mock
  protected MessageDto messageDto;
  @Mock
  protected User testUser;
  @Mock
  protected MessageChannel messageChannel;
  @Mock
  protected Message message;
  @Mock
  private Guild guild;

  /**
   * Global init method.
   */
  @BeforeEach
  public void init() {
    when(realMessageReceivedEvent.getChannel())
        .thenReturn(messageChannel);
    when(realMessageReceivedEvent.getMessage())
        .thenReturn(message);
    when(realMessageReceivedEvent.getMessageId())
        .thenReturn("9876543210");
    when(realMessageReceivedEvent.getAuthor())
        .thenAnswer(invocation -> message.getAuthor());
    when(realMessageReceivedEvent.getGuild())
        .thenReturn(guild);
    when(guild.getName())
        .thenReturn("Local Test Guild");

    when(messageReceivedEvent.getChannel())
        .thenReturn(channelDto);
    when(messageReceivedEvent.getAuthor())
        .thenReturn(authorDto);
    when(messageReceivedEvent.getMessage())
        .thenReturn(messageDto);

    when(testUser.isBot())
        .thenReturn(Boolean.FALSE);
    when(testUser.getName())
        .thenReturn("Test user");
  }
}
