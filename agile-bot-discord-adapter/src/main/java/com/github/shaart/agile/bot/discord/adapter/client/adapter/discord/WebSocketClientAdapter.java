package com.github.shaart.agile.bot.discord.adapter.client.adapter.discord;

import com.github.shaart.agile.bot.discord.adapter.context.ThreadLocalContext;
import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.requests.WebSocketClient;

public class WebSocketClientAdapter extends WebSocketClient {

  private static final String REMEMBER_EVENT_TYPE = "MESSAGE_CREATE";
  private static final String DISCORD_EVENT_TYPE_KEY = "t";

  private final ThreadLocalContext threadLocalContext = ThreadLocalContext.getInstance();

  public WebSocketClientAdapter(JDAImpl api, Compression compression, int gatewayIntents,
      GatewayEncoding encoding) {

    super(api, compression, gatewayIntents, encoding);
  }

  @Override
  protected void handleEvent(DataObject content) {
    boolean shouldRemember = shouldRemember(content);
    if (shouldRemember) {
      threadLocalContext.getDiscordEventContext().set(content);
    }
    super.handleEvent(content);
    if (shouldRemember) {
      threadLocalContext.clearContext();
    }
  }

  private boolean shouldRemember(DataObject content) {
    String eventType = content.getString(DISCORD_EVENT_TYPE_KEY);
    return REMEMBER_EVENT_TYPE.equals(eventType);
  }
}
