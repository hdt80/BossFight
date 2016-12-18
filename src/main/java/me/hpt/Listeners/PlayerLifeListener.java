package me.hpt.Listeners;

import com.connorlinfoot.titleapi.TitleAPI;
import me.hpt.BossFight;
import me.hpt.BossFightManager;
import me.hpt.Messages;
import me.hpt.Runnables.RespawnRunnable;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.Plugin;

public class PlayerLifeListener implements Listener {

	private Plugin plugin; // Plugin we're register under

	/**
	 * Ctor
	 * @param p Plugin to register under
	 */
	public PlayerLifeListener(Plugin p) {
		plugin = p;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Checking for when a player dies
	 */
	@EventHandler
	public void playerHitEvent(EntityDamageEvent e) {
		if (!BossFightManager.running()) {
			return;
		}

		if (!(e.getEntity() instanceof Player)) {
			return;
		}

		final Player p = (Player) e.getEntity();

		if (p.getHealth() - e.getFinalDamage() <= 0) {
			e.setCancelled(true);

			p.setGameMode(GameMode.SPECTATOR);

			int respawnTime = 5 * 20; // In ticks

			new RespawnRunnable(p, BossFightManager.getRespawnLocation()).runTaskLater(BossFight.get(), respawnTime);
			TitleAPI.sendTitle(p, 5, respawnTime - 10, 5, Messages.get("Respawn-Title"), Messages.get("Respawn-Subtitle", respawnTime / 20));
		}

		BossFightManager.updateFighterInfo(p, p.getHealth() - e.getFinalDamage());
	}

	/**
	 * When a player's life total goes up make sure to track that
	 */
	@EventHandler
	public void playerRegainLife(EntityRegainHealthEvent e) {
		if (!(e.getEntity() instanceof Player) || !BossFightManager.running()) {
			return;
		}

		BossFightManager.updateFighterInfo((Player) e.getEntity(), ((Player) e.getEntity()).getHealth() + e.getAmount());
	}
}
