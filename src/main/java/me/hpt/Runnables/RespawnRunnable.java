package me.hpt.Runnables;

import me.hpt.BossFightManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnRunnable extends BukkitRunnable {

	private Player who; // Who to respawn
	private Location where; // Where to respawn them

	public RespawnRunnable(Player p, Location loc) {
		this.who = p;
		this.where = loc;
	}

	public void run() {
		who.teleport(BossFightManager.getRespawnLocation());
		who.setGameMode(GameMode.ADVENTURE);

		// All nice and healthy
		who.setHealth(20.0);
		who.setSaturation(10.0f);
		who.setFoodLevel(20);
	}
}
