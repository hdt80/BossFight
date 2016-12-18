package me.hpt.Listeners;

import me.hpt.BossFightManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
			}
			e.setCancelled(true);
		}
	}
}
