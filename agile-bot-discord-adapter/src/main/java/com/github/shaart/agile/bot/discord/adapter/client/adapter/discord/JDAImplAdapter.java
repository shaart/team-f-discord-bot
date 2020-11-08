package com.github.shaart.agile.bot.discord.adapter.client.adapter.discord;

import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.requests.WebSocketClient;
import net.dv8tion.jda.internal.utils.Checks;
import net.dv8tion.jda.internal.utils.config.AuthorizationConfig;
import net.dv8tion.jda.internal.utils.config.MetaConfig;
import net.dv8tion.jda.internal.utils.config.SessionConfig;
import net.dv8tion.jda.internal.utils.config.ThreadingConfig;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.security.auth.login.LoginException;

/**
 * Adapter of {@link JDAImpl} to use {@link WebSocketClientAdapter} instead of default {@link
 * WebSocketClient}.
 */
public class JDAImplAdapter extends JDAImpl {

  public JDAImplAdapter(AuthorizationConfig authConfig,
      SessionConfig sessionConfig,
      ThreadingConfig threadConfig,
      MetaConfig metaConfig) {
    super(authConfig, sessionConfig, threadConfig, metaConfig);
  }

  /**
   * In this implementation {@link WebSocketClient} is changed to {@link WebSocketClientAdapter}.
   * <br/><br/> See {@link JDAImpl#login(String, ShardInfo, Compression, boolean, int,
   * GatewayEncoding)}
   */
  @SuppressWarnings("ConstantConditions")
  @Override
  public int login(String gatewayUrl, ShardInfo shardInfo, Compression compression,
      boolean validateToken, int intents, GatewayEncoding encoding) throws LoginException {
    this.shardInfo = shardInfo;
    threadConfig.init(this::getIdentifierString);
    requester.getRateLimiter().init();
    this.gatewayUrl = gatewayUrl == null ? getGateway() : gatewayUrl;
    Checks.notNull(this.gatewayUrl, "Gateway URL");

    String token = authConfig.getToken();
    setStatus(Status.LOGGING_IN);
    if (token == null || token.isEmpty()) { //NOSONAR use JDA library "as is"
      throw new LoginException("Provided token was null or empty!");
    }

    Map<String, String> previousContext = null;
    ConcurrentMap<String, String> contextMap = metaConfig.getMdcContextMap();
    if (contextMap != null) {
      if (shardInfo != null) {
        contextMap.put("jda.shard", shardInfo.getShardString());
        contextMap.put("jda.shard.id", String.valueOf(shardInfo.getShardId()));
        contextMap.put("jda.shard.total", String.valueOf(shardInfo.getShardTotal()));
      }
      // set MDC metadata for build thread
      previousContext = MDC.getCopyOfContextMap();
      contextMap.forEach(MDC::put);
      requester.setContextReady(true);
    }
    if (validateToken) {
      verifyToken();
      LOG.info("Login Successful!");
    }

    client = new WebSocketClientAdapter(this, compression, intents, encoding);
    // remove our MDC metadata when we exit our code
    if (previousContext != null) {
      previousContext.forEach(MDC::put);
    }

    if (shutdownHook != null) {
      Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    return shardInfo == null ? -1 : shardInfo.getShardTotal();
  }
}
