package com.github.shaart.agile.bot;

import static org.mockito.Mockito.when;

import com.github.shaart.agile.bot.component.MessageSender;
import com.github.shaart.agile.bot.dto.AuthorDto;
import com.github.shaart.agile.bot.dto.ChannelDto;
import com.github.shaart.agile.bot.dto.EventDto;
import com.github.shaart.agile.bot.dto.MessageDto;
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

  @Mock
  protected EventDto messageReceivedEvent;
  @Mock
  protected ChannelDto channelDto;
  @Mock
  protected AuthorDto authorDto;
  @Mock
  protected MessageDto messageDto;

  /**
   * Global init method.
   */
  @BeforeEach
  public void init() {
    when(messageReceivedEvent.getChannel())
        .thenReturn(channelDto);
    when(messageReceivedEvent.getAuthor())
        .thenReturn(authorDto);
    when(messageReceivedEvent.getMessage())
        .thenReturn(messageDto);
  }
}
