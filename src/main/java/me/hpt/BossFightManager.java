package me.hpt;

import com.connorlinfoot.titleapi.TitleAPI;
import me.hpt.Scoreboard.BetterScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BossFightManager {

	private static ArrayList<Player> trackedPlayers; // Players being tracked
	private static Player playerBoss; // Player who is being called the boss

	private static boolean started = false; // If a boss fight has been started

	private static int currentHits = 0; // How many hits the players have landed on the boss
	private static int maxHits = 0; // How many hits the players needs to land to defeat the boss

	private static Location respawnLocation;

	private static BossBar bar;

	private static BetterScoreboard scoreboard;

	static {
		trackedPlayers = new ArrayList<Player>();
		playerBoss = null;
		respawnLocation = null;
		bar = null;
		scoreboard = null;
	}

	/**
	 * Begin a boss fight
	 * @param boss Who the boss is
	 * @param hits How many hits it will take to defeat the boss
	 */
	public static void startFight(Player boss, int hits) {
		if (playerBoss != null) {
			Logger.error("Failed to startFight. Player is already set");
			return;
		}

		if (respawnLocation == null) {
			boss.sendMessage("Respawn location is not set! Cannot start");
			return;
		}

		started = true;

		maxHits = hits;
		currentHits = maxHits;
		playerBoss = boss;

		bar = new BossBar("", BarColor.RED, BarStyle.SOLID);

		scoreboard = new BetterScoreboard(playerBoss, "Fighters");

		// Will include the boss
		List<Player> players = boss.getWorld().getPlayers();
		for (Player p : players) {
			addPlayer(p);
		}

		// Set up the boss
		playerBoss.setAllowFlight(true);

		// Set up the world
		World w = boss.getWorld();
		w.setGameRuleValue("keepInventory", "true");

		createFighterInfo();
	}

	/**
	 * End the boss fight
	 * @param forced If the end of the fight was forced (By a command or otherwise). If forced is false it is assumed
	 *               the fight was ended by killing the boss
	 */
	public static void endFight(boolean forced) {

		// Inform the players the game has ended
		String endMessage = ChatColor.RED + "Boss defeated!";
		if (forced) {
			endMessage = ChatColor.YELLOW + "Fight ended";
		}
		for (Player p : trackedPlayers) {
			TitleAPI.clearTitle(p);
			TitleAPI.sendTitle(p, 20, 200, 20, endMessage, null);
		}

		// Remove all the players in the fight
		for (Player p : trackedPlayers) {
			removePlayer(p);
		}
		trackedPlayers.clear();

		// Reset for the next fight
		playerBoss = null;
		started = false;
	}

	/**
	 * Get how close the boss is to defeat, as a percent
	 * @return Percentage representing how close the boss is to defeat
	 */
	private static float getRatioDone() {
		return ((float) currentHits / (float) maxHits) * 100.0f;
	}

	private static String getStringDone() {
		return String.format("Health (%d/%d)", currentHits, maxHits);
	}

	/**
	 * Get the Player who is the boss
	 * @return trackerboss
	 */
	public static Player getBoss() {
		return playerBoss;
	}

	/**
	 * Begin tracking a Player
	 * @param p Player to track
	 */
	public static void addPlayer(Player p) {
		if (!trackedPlayers.contains(p)) {
			if (started) {
				bar.addPlayer(p);
				updateBar();
				//BarAPI.setMessage(p, getStringDone(), getRatioDone());
			}

			trackedPlayers.add(p);
		}
	}

	/**
	 * Stop tracking a Player
	 * @param p Player to stop tracking
	 */
	public static void removePlayer(Player p) {
		if (trackedPlayers.contains(p)) {
			//BarAPI.removeBar(p);
			bar.removePlayer(p);
			trackedPlayers.remove(p);
			updateBar();
		} else {
			Bukkit.broadcastMessage("failed to find");
		}
	}

	/**
	 * Get if a Player is being tracked by this BossBar
	 * @param p Player to check
	 * @return If the player is contained within trackedPlayers
	 */
	public static boolean hasPlayer(Player p) {
		return trackedPlayers.contains(p);
	}

	/**
	 * Check if a boss fight is currently happening
	 * @return started
	 */
	public static boolean running() {
		return started;
	}

	/**
	 * Decrease the amount of hits the boss has left and update the bar
	 */
	public static void decCounter() {
		if (!running()) {
			return;
		}
		--currentHits;
		updateBar();
	}

	/**
	 * Increase the amount of hits the boss has left and update the bar
	 */
	public static void incCounter() {
		if (!running()) {
			return;
		}
		++currentHits;
		updateBar();
	}

	/**
	 * Update the boss bar for all players in the fight
	 */
	private static void updateBar() {
		if (getRatioDone() <= 0.0f) {
			endFight(false);
			return;
		}

		// Update the bar for all tracked players
		for (Player p : trackedPlayers) {
			bar.setTitle(getStringDone());
			bar.setProgress(getRatioDone() / 100.0f);
			//BarAPI.setMessage(p, getStringDone(), getRatioDone());
		}
	}

	private static void createFighterInfo() {
		Player p;
		for (int i = 0; i < trackedPlayers.size(); ++i) {
			p = trackedPlayers.get(i);
			scoreboard.setLine(i, String.format("%s : %f", p.getName(), p.getHealth()));
		}

		scoreboard.build();
		scoreboard.send();
	}

	/**
	 * Update a Fighter's info
	 * @param p Player to update
	 * @param newHealth New health of the fighter
	 */
	public static void updateFighterInfo(Player p, double newHealth) {
		int index = trackedPlayers.indexOf(p);

		if (index == -1) {
			Logger.warn("Player %s is not tracked.", p.getName());
			return;
		}

		scoreboard.setLine(index, String.format("%s : %g", p.getName(), newHealth));
		scoreboard.update();
	}

	/**
	 * Get the location players will respawn at when they die
	 * @return respawnLocation
	 */
	public static Location getRespawnLocation() {
		return respawnLocation;
	}

	/**
	 * Set the location players will respawn at when they die
	 * @param loc Location to set respawnLocation to
	 */
	public static void setRespawnLocation(Location loc) {
		respawnLocation = loc;
	}
}
