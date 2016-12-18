package me.hpt.Listeners;

import me.hpt.BossFightManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerTrackingListener implements Listener {

	private Plugin plugin;

	public PlayerTrackingListener(Plugin p) {
		plugin = p;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * When a player joins, add them to the fight
	 */
	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		if (!BossFightManager.running()) {
			return;
		}
		// If they're in the same world as the fight add them
		if (BossFightManager.getBoss().getWorld() == e.getPlayer().getWorld()) {
			BossFightManager.addPlayer(e.getPlayer());
		}
	}

	/**
	 * When a player leaves the game, remove them from the fight
	 */
	@EventHandler
	public void playerLeave(PlayerQuitEvent e) {
		if (!BossFightManager.running()) {
			return;
		}
		// They've left, so remove them from the fight
		if (BossFightManager.hasPlayer(e.getPlayer())) {
			BossFightManager.removePlayer(e.getPlayer());
		}
	}
}
