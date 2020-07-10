package com.github.shaart.team.f.discord.bot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * A command to be processed by bot.
 */
public interface BotCommand {

  /**
   * Minimal required arguments count for correct run.
   */
  int minArgumentsCount();

  /**
   * Command's name/alias to be captured by bot.
   */
  String getAlias();

  /**
   * Command description for 'help' command.
   */
  String getDescription();

  /**
   * Get command's usage text in format.
   *
   * <pre>
   * ```
   * command arg1 arg2
   * ```
   * </pre>
   */
  String getUsage();

  /**
   * Process command event with arguments.
   *
   * @param event a message event
   * @param args  parsed command's arguments if present (or empty array if none)
   */
  void run(MessageReceivedEvent event, String... args);
}
