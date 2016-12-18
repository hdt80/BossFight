package me.hpt;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Messages {

	private static Map<String, String> messages;

	static {
		messages = new HashMap<>();
	}

	/**
	 * Load messages from a file
	 * @param plugin Plugin to load from
	 * @param fileName Filename, extension included
	 */
	public static void load(Plugin plugin, String fileName) {
		try {

			Logger.info("Loading messages from: %s", fileName);

			messages.clear();
			File file = new File(plugin.getDataFolder(), fileName);
			if (!file.exists()) {
				// Load the embedded Messages.lang
				plugin.saveResource(fileName, true);
			}

			// Add the lines found in the file
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (String line; (line = br.readLine()) != null;) {
				// Lines starting with a # or that are empty are ignored
				if (!line.startsWith("#") && !line.isEmpty()) {
					String[] split = line.split("=");
					messages.put(split[0], split[1]);
				}
			}
		} catch (FileNotFoundException e) {
			Logger.error("Cannot load Messages. FileNotFoundException: %s", e.toString());
		} catch (IOException e) {
			Logger.error("Cannot load Messages. IOException: %s", e.toString());
		}
	}

	/**
	 * Get a String from the lang file
	 * @param key  Local name of the String
	 * @param args Arguments that will be filled from the message
	 * @return String formatted with arguments
	 */
	public static String get(String key, Object... args) {
		String msg = "No string found for \"" + key + "\".";
		if (messages.containsKey(key)) {
			msg = messages.get(key);
		} else {
			return msg;
		}
		for (int i = 0; i < args.length; i++) {
			msg = msg.replace("{" + i + "}", args[i].toString());
		}
		msg = ChatColor.translateAlternateColorCodes('`', msg);
		return msg;
	}
}
