package me.hpt;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Config {

	// Bukkit's config file
	private static FileConfiguration config;

	/**
	 * Get Bukkit's config file
	 * @return config
	 */
	public static FileConfiguration get() {
		return config;
	}

	/**
	 * Load a Plugin's config file
	 * @param p Plugin to load the config of
	 */
	public static void load(Plugin p) {
		p.saveDefaultConfig();
		config = p.getConfig();
	}

	/**
	 * Save the config file
	 */
	public static void save() {
		try {
			config.save(new File(BossFight.get().getDataFolder(), "config.yml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
