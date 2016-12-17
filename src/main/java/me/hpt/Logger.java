package me.hpt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Simple logger class
 */
public class Logger {
	private static java.util.logging.Logger logger = null;
	private static JavaPlugin plugin;

	public static void setPlugin(JavaPlugin p) {
		plugin = p;
	}

	public static void setLogger(java.util.logging.Logger logger) {
		Logger.logger = logger;
	}

	public static void broadcast(String msg, Object... args) {
		Bukkit.broadcastMessage(String.format(msg, args));
	}

	public static void debug(String msg, Object... args) {
		logger.info(String.format(msg, args));
	}

	public static void info(String msg, Object... args) {
		logger.info(String.format(msg, args));
	}

	public static void warn(String msg, Object... args) {
		logger.warning(String.format(msg, args));
	}

	public static void error(String msg, Object... args) {
		logger.severe(String.format(msg, args));
	}

	public static void fatal(String msg) {
		logger.severe("============ FATAL ERROR. CANNOT RECOVER ============");
		logger.severe(msg);
		logger.severe("=====================================================");
		BossFight.get().getServer().shutdown();
	}
}
