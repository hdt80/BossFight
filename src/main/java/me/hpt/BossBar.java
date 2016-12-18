package me.hpt;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.*;

public class BossBar {

	// Bukkit's BossBar
	private org.bukkit.boss.BossBar bar;

	// Players viewing this BossBar
	private List<Player> players;

	// Map of what Player's have what Bars
	private static Map<UUID, BossBar> bars;

	static {
		bars = new HashMap<UUID, BossBar>();
	}

	/**
	 * Get the BossBar assigned to a Player
	 * @param p Player to check for
	 * @return The BossBar a Player has, or null if no BossBar is set
	 */
	public static BossBar getBossBar(Player p) {
		if (bars.containsKey(p.getUniqueId())) {
			return bars.get(p.getUniqueId());
		}
		return null;
	}

	/**
	 * If a Player has a BossBar, clear it
	 * @param p Player to clear the BossBar of
	 */
	public static void clearBar(Player p) {
		BossBar bar = getBossBar(p);

		if (bar != null) {
			bar.removePlayer(p);
		}
	}

	/**
	 * Clear the bars off of all players
	 */
	public static void clearAll() {
		for (Map.Entry<UUID, BossBar> entry : bars.entrySet()) {
			entry.getValue().clear();
		}
	}

	/**
	 * Ctor for BossBar
	 * @param title Title of the BossBar
	 * @param color Color to use in the BossBar
	 * @param style Style to use in the BossBar
	 */
	public BossBar(String title, BarColor color, BarStyle style) {
		bar = Bukkit.createBossBar(title, color, style);
		bar.setTitle(title);
		bar.setColor(color);
		bar.setStyle(style);

		players = new ArrayList<Player>();
	}

	/**
	 * Add a Player to viewing this BossBar
	 * @param p Player to add
	 */
	public void addPlayer(Player p) {
		if (hasPlayer(p)) {
			return;
		}

		bar.addPlayer(p);
		players.add(p);

		bars.put(p.getUniqueId(), this);
	}

	/**
	 * Remove a Player from viewing this BossBar
	 * @param p Player to remove
	 */
	public void removePlayer(Player p) {
		if (!hasPlayer(p)) {
			return;
		}

		bar.removePlayer(p);
		players.remove(p);
	}

	/**
	 * Remove all players from this BossBar
	 */
	public void clear() {
		bar.removeAll();
	}

	/**
	 * Check if a Player is viewing this BossBar
	 * @param p Player to check for
	 * @return If players contains p
	 */
	public boolean hasPlayer(Player p) {
		return players.contains(p);
	}

	/**
	 * Get the title used by Bukkit's BossBar
	 * @return bar.getTitle()
	 */
	public String getTitle() {
		return bar.getTitle();
	}

	/**
	 * Set the title to be used by Bukkit's BossBar
	 * @param title Title to use
	 */
	public void setTitle(String title) {
		bar.setTitle(title);
	}

	/**
	 * Get the progress of the BossBar, 0 being empty and 1 being full
	 * @return bar.getProgress()
	 */
	public double getProgress() {
		return bar.getProgress();
	}

	/**
	 * Set the progress of the BossBar, 0 being emtpy and 1 being full
	 * @param prog Progress to set
	 */
	public void setProgress(double prog) {
		bar.setProgress(prog);
	}
}
