package me.hpt.Scoreboard;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class BetterScoreboardManager {

	// Map of Players and their Scoreboards
	private static Map<UUID, BetterScoreboard> boards;

	static {
		boards = Maps.newHashMap();
	}

	/**
	 * Add a Player's BetterScoreboard to the manager
	 * @param board Board to add
	 */
	public static void addBoard(BetterScoreboard board) {
		if (!boards.containsKey(board.getPlayer().getUniqueId())) {
			boards.put(board.getPlayer().getUniqueId(), board);
		}
	}

	/**
	 * Remove a Player from the manager
	 * @param p Player to remove
	 * @return If the operation was successful
	 */
	public static boolean remove(Player p) {
		if (boards.containsKey(p.getUniqueId())) {
			boards.remove(p.getUniqueId());
			return true;
		}
		return false;
	}

	/**
	 * Remove a Player from the manager
	 * @param board Board of the Player
	 * @return If the operation was successful
	 */
	public static boolean removeBoard(BetterScoreboard board) {
		if (boards.containsKey(board.getPlayer().getUniqueId())) {
			boards.remove(board.getPlayer().getUniqueId());
			return true;
		}
		return false;
	}

	/**
	 * Return the Board that is managed by this manager
	 * @param p Player to get the board of
	 * @return The BetterScoreboard of the Player, or null if it doesn't exist
	 */
	public static BetterScoreboard getBoard(Player p) {
		if (boards.containsKey(p.getUniqueId())) {
			return boards.get(p.getUniqueId());
		}
		return null;
	}

	/**
	 * Update the Board of that Player
	 * @param p Player whose Scoreboard to update
	 */
	public static void update(Player p) {
		if (boards.containsKey(p.getUniqueId())) {
			boards.get(p.getUniqueId()).update();
		}
	}

	/**
	 * Create a Board for that Player
	 * @param p Player to create the board for
	 * @param title Title of the BetterScoreboard
	 */
	public static void create(Player p, String title) {
		boards.put(p.getUniqueId(), new BetterScoreboard(p, title));
	}

	/**
	 * Set a Line of a Player's BetterScoreboard
	 * @param p Player instance of the BeterScoreboard that's gonna be changed
	 * @param line Line number to change
	 * @param text Text to change line number to
	 */
	public static void setLine(Player p, int line, String text) {
		if (boards.containsKey(p.getUniqueId())) {
			boards.get(p.getUniqueId()).setLine(line, text);
		}
	}

	/**
	 * Get a Line of a Player's BetterScoreboard
	 * @param p Player instance of the Scoreboard
	 * @param line Line number to get
	 * @return The String on the BetterScoreboard
	 */
	public static String getLine(Player p, int line) {
		if (boards.containsKey(p.getUniqueId())) {
			return boards.get(p.getUniqueId()).getLine(line);
		}
		return "PLAYER NOT FOUND";
	}

	/**
	 * Build a Scoreboard, once built it cannot be rebuilt
	 * @param p Player to build
	 */
	public static void build(Player p){
		if (boards.containsKey(p.getUniqueId())) {
			boards.get(p.getUniqueId()).build();
		}
	}

	/**
	 * Reset a Player's board
	 * @param p Player to reset
	 */
	public static void reset(Player p) {
		if (boards.containsKey(p.getUniqueId())) {
			boards.get(p.getUniqueId()).resetScores();
		}
	}

	/**
	 * Send the Player their BetterScoreboard
	 * @param p Player to send
	 */
	public static void send(Player p) {
		if (boards.containsKey(p.getUniqueId())) {
			boards.get(p.getUniqueId()).send();
		}
	}
}
