package me.hpt.Listeners;

import com.connorlinfoot.titleapi.TitleAPI;
import me.hpt.BossFightManager;
import me.hpt.BossFight;
import me.hpt.Runnables.RespawnRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class BossHitListener implements Listener {

	private Plugin plugin;

	public BossHitListener(Plugin p) {
		p.getServer().getPluginManager().registerEvents(this, p);
		plugin = p;
	}

	/**
	 * When the boss is hit, decrement the boss' health by 1
	 */
	@EventHandler
	public void onEntityHit(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player) || !BossFightManager.running()) {
			return;
		}

		Player hit = (Player) e.getEntity();

		if (hit.getUniqueId() == BossFightManager.getBoss().getUniqueId()) {
			if (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE
					|| e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
				BossFightManager.decCounter();
				hit.sendMessage("Hit: " + e.getCause().name());

				e.setCancelled(true);
			}
		}
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

	@EventHandler
	public void playerRegainLife(EntityRegainHealthEvent e) {
		if (!(e.getEntity() instanceof Player) || !BossFightManager.running()) {
			return;
		}

		BossFightManager.updateFighterInfo((Player) e.getEntity(), ((Player) e.getEntity()).getHealth() + e.getAmount());
	}

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

			Bukkit.broadcastMessage(e.getCause().toString());

			p.setGameMode(GameMode.SPECTATOR);

			int respawnTime = 100; // In ticks

			new RespawnRunnable(p, BossFightManager.getRespawnLocation()).runTaskLater(BossFight.get(), respawnTime);

			TitleAPI.sendTitle(p, 5, respawnTime - 10, 5, ChatColor.RED + "You died!", ChatColor.RED + "3 seconds to respawn");
		}

		BossFightManager.updateFighterInfo(p, p.getHealth() - e.getFinalDamage());
	}
}
