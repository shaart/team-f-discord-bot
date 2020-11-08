package com.github.shaart.agile.bot.discord.adapter.context;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.utils.data.DataObject;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadLocalContext {

  private static final ThreadLocalContext INSTANCE = new ThreadLocalContext();

  private final ThreadLocal<DataObject> discordEventContext = new ThreadLocal<>();

  public static ThreadLocalContext getInstance() {
    return INSTANCE;
  }

  public void clearContext() {
    discordEventContext.remove();
  }
}
