package com.github.shaart.agile.bot.discord.adapter.client.adapter.discord;

import com.neovisionaries.ws.client.WebSocketFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.ConcurrentSessionController;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.managers.PresenceImpl;
import net.dv8tion.jda.internal.requests.WebSocketClient;
import net.dv8tion.jda.internal.utils.IOUtil;
import net.dv8tion.jda.internal.utils.config.AuthorizationConfig;
import net.dv8tion.jda.internal.utils.config.MetaConfig;
import net.dv8tion.jda.internal.utils.config.SessionConfig;
import net.dv8tion.jda.internal.utils.config.ThreadingConfig;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;

/**
 * Adapter to use {@link WebSocketClientAdapter} instead of {@link WebSocketClient}.<br/><br/> This
 * is necessary to save the request in context for creation and delivering responses.
 */
public class JDABuilderAdapter extends JDABuilder {

  public JDABuilderAdapter(String botToken, int intents) {
    //noinspection deprecation
    super(botToken); //NOSONAR there is no another way to create extension for JDABuilder
    this.intents = 1 | intents;
  }

  @NotNull
  @CheckReturnValue
  public static JDABuilder create(@Nullable String botToken,
      @NotNull Collection<GatewayIntent> gatewayIntents) {

    return new JDABuilderAdapter(botToken, GatewayIntent.getRaw(gatewayIntents))
        .applyAdapterIntents();
  }

  /**
   * In this implementation {@link JDAImpl} is changed to {@link JDAImplAdapter}.<br/> See {@link
   * JDABuilder#build()}
   */
  @Nonnull
  @Override
  public JDA build() throws LoginException {
    checkAdapterIntents();
    OkHttpClient httpClient = this.httpClient;
    if (httpClient == null) {
      if (this.httpClientBuilder == null) {
        this.httpClientBuilder = IOUtil.newHttpClientBuilder();
      }
      httpClient = this.httpClientBuilder.build();
    }

    WebSocketFactory wsFactory = this.wsFactory == null ? new WebSocketFactory() : this.wsFactory;

    if (controller == null && shardInfo != null) {
      controller = new ConcurrentSessionController();
    }

    AuthorizationConfig authConfig = new AuthorizationConfig(token);
    ThreadingConfig threadingConfig = new ThreadingConfig();
    threadingConfig.setCallbackPool(callbackPool, shutdownCallbackPool);
    threadingConfig.setGatewayPool(mainWsPool, shutdownMainWsPool);
    threadingConfig.setRateLimitPool(rateLimitPool, shutdownRateLimitPool);
    threadingConfig.setEventPool(eventPool, shutdownEventPool);
    SessionConfig sessionConfig = new SessionConfig(controller, httpClient, wsFactory,
        voiceDispatchInterceptor, flags, maxReconnectDelay, largeThreshold);
    MetaConfig metaConfig = new MetaConfig(maxBufferSize, contextMap, cacheFlags, flags);

    JDAImpl jda = new JDAImplAdapter(authConfig, sessionConfig, threadingConfig, metaConfig);
    jda.setMemberCachePolicy(memberCachePolicy);
    // We can only do member chunking with the GUILD_MEMBERS intent
    if ((intents & GatewayIntent.GUILD_MEMBERS.getRawValue()) == 0) {
      jda.setChunkingFilter(ChunkingFilter.NONE);
    } else {
      jda.setChunkingFilter(chunkingFilter);
    }

    if (eventManager != null) {
      jda.setEventManager(eventManager);
    }

    if (audioSendFactory != null) {
      jda.setAudioSendFactory(audioSendFactory);
    }

    listeners.forEach(jda::addEventListener);
    jda.setStatus(
        JDA.Status.INITIALIZED);  //This is already set by JDA internally, but this is to make sure the listeners catch it.

    // Set the presence information before connecting to have the correct information ready when sending IDENTIFY
    ((PresenceImpl) jda.getPresence())
        .setCacheActivity(activity)
        .setCacheIdle(idle)
        .setCacheStatus(status);
    jda.login(shardInfo, compression, true, intents, encoding);
    return jda;
  }

  /**
   * This method is just copied and renamed.<br/> See {@link JDABuilder#checkIntents()}
   */
  @SuppressWarnings("JavadocReference")
  private void checkAdapterIntents() {
    boolean membersIntent = (intents & GatewayIntent.GUILD_MEMBERS.getRawValue()) != 0;
    if (!membersIntent && memberCachePolicy == MemberCachePolicy.ALL) {
      throw new IllegalStateException(
          "Cannot use MemberCachePolicy.ALL without GatewayIntent.GUILD_MEMBERS enabled!");
    } else if (!membersIntent && chunkingFilter != ChunkingFilter.NONE) {
      JDAImpl.LOG.warn("Member chunking is disabled due to missing GUILD_MEMBERS intent.");
    }

    if (!automaticallyDisabled.isEmpty()) {
      if (JDAImpl.LOG.isWarnEnabled()) {
        JDAImpl.LOG.warn("Automatically disabled CacheFlags due to missing intents");
        // List each missing intent
        automaticallyDisabled.stream()
            .map(it -> "Disabled CacheFlag." + it
                + " (missing GatewayIntent." + it.getRequiredIntent() + ")")
            .forEach(JDAImpl.LOG::warn);

        // Tell user how to disable this warning
        String cacheFlagsJoined = automaticallyDisabled.stream()
            .map(it -> "CacheFlag." + it)
            .collect(Collectors.joining(", "));
        JDAImpl.LOG.warn("You can manually disable these flags to remove this warning by using "
                + "disableCache({}) on your JDABuilder",
            cacheFlagsJoined);
      }
      // Only print this warning once
      automaticallyDisabled.clear();
    }

    if (cacheFlags.isEmpty()) {
      return;
    }

    EnumSet<GatewayIntent> providedIntents = GatewayIntent.getIntents(intents);
    for (CacheFlag flag : cacheFlags) {
      GatewayIntent intent = flag.getRequiredIntent();
      if (intent != null && !providedIntents.contains(intent)) {
        throw new IllegalArgumentException(
            "Cannot use CacheFlag." + flag + " without GatewayIntent." + intent + "!");
      }
    }
  }

  /**
   * This method is just copied, renamed and it's return type is changed from {@link JDABuilder} to
   * {@link JDABuilderAdapter}.<br/> See {@link JDABuilder#applyIntents()}
   */
  @SuppressWarnings("JavadocReference")
  private JDABuilderAdapter applyAdapterIntents() {
    EnumSet<CacheFlag> disabledCache = EnumSet.allOf(CacheFlag.class);
    for (CacheFlag flag : CacheFlag.values()) {
      GatewayIntent requiredIntent = flag.getRequiredIntent();
      if (requiredIntent == null || (requiredIntent.getRawValue() & intents) != 0) {
        disabledCache.remove(flag);
      }
    }

    boolean enableMembers = (intents & GatewayIntent.GUILD_MEMBERS.getRawValue()) != 0;
    return setChunkingFilter(enableMembers ? ChunkingFilter.ALL : ChunkingFilter.NONE)
        .setMemberCachePolicy(enableMembers ? MemberCachePolicy.ALL : MemberCachePolicy.DEFAULT)
        .setAdapterDisabledCache(disabledCache);
  }

  /**
   * This method is just copied, renamed and it's return type is changed from {@link JDABuilder} to
   * {@link JDABuilderAdapter}.<br/> See {@link JDABuilder#setDisabledCache(EnumSet)} ()}
   */
  @SuppressWarnings("JavadocReference")
  private JDABuilderAdapter setAdapterDisabledCache(EnumSet<CacheFlag> flags) {
    disableCache(flags);
    this.automaticallyDisabled.addAll(flags);
    return this;
  }

  /**
   * This method is just has changed return type is changed from {@link JDABuilder} to {@link
   * JDABuilderAdapter}.<br/> See {@link JDABuilder#setChunkingFilter(ChunkingFilter)}
   */
  @Nonnull
  @Override
  public JDABuilderAdapter setChunkingFilter(@Nullable ChunkingFilter filter) {
    return (JDABuilderAdapter) super.setChunkingFilter(filter);
  }

  /**
   * This method is just has changed return type is changed from {@link JDABuilder} to {@link
   * JDABuilderAdapter}.<br/> See {@link JDABuilder#setMemberCachePolicy(MemberCachePolicy)}
   */
  @Nonnull
  @Override
  public JDABuilderAdapter setMemberCachePolicy(@Nullable MemberCachePolicy policy) {
    return (JDABuilderAdapter) super.setMemberCachePolicy(policy);
  }
}
