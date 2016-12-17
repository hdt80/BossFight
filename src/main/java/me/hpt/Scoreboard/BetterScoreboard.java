package me.hpt.Scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Map;

public class BetterScoreboard {

	private Scoreboard scoreboard; // Bukkit's Scoreboard
	private Objective objective;
	private List<Team> teams;
	private Map<Integer, String> scores; // Text of the Scoreboard
	private String displayName; // Display name of the Scoreboard
	private final Player player; // Player this Scoreboard is being displayed to

	/**
	 * Ctor
	 * @param player Player to display this Scoreboard to
	 * @param displayName Name to display this Scoreboard as
	 */
	public BetterScoreboard(Player player, String displayName) {
		this.player = player;
		this.displayName = displayName;

		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = scoreboard.registerNewObjective("scoreboard", "dummy");

		objective.setDisplayName(this.displayName);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		scores = Maps.newLinkedHashMap();
		teams = Lists.newArrayList();
	}

	/**
	 * Set a line in the Scoreboard
	 * @param line Line to set, 0 indexed
	 * @param text Text to set. Text after the 48th char will be cut off
	 */
	public void setLine(int line, String text) {
		scores.put(line, fixText(text));
	}

	/**
	 * Set a line to blank text
	 * @param line Line to set, 0 indexed
	 */
	public void setBlank(int line) {
		scores.put(line, fixText(" "));
	}

	/**
	 * Get the text of a specific line
	 * @param line Line to get, 0 indexed
	 * @return Text on a line, or an empty String if that line is not set
	 */
	public String getLine(int line) {
		if (scores.containsKey(line)) {
			return scores.get(line);
		}
		return "";
	}

	/**
	 * Fix the text, making it ready for the Scoreboard
	 * @param text Text to spruce up
	 * @return Text that is formatted perfectly for a Scoreboard
	 */
	private String fixText(String text) {
		for (int i : scores.keySet()) {
			String s = scores.get(i);
			if (s.equalsIgnoreCase(text)) {
				text += "§r";
			}
		}
		if (text.length() > 48) {
			text = text.substring(0, 47);
		}
		return text;
	}

	/**
	 * Once all the lines of the text have been added, build the Scoreboard
	 * Once build() is called this Scoreboard is ready to be displayed
	 */
	public void build() {
		int index = scores.size();
		for (Map.Entry<Integer, String> entry : scores.entrySet()) {
			Integer score = (entry.getKey()) != null ? entry.getKey() : index;
			objective.getScore(entry.getValue()).setScore(score);
			--index;
		}
	}

	/**
	 * Send the Scoreboard to the Player
	 */
	public void send() {
		player.setScoreboard(scoreboard);
	}

	/**
	 * Update the Scoreboard with text that has been added since the last update
	 */
	public void update() {
		if (player.getScoreboard() != null) {
			for (String s : player.getScoreboard().getEntries()) {
				player.getScoreboard().resetScores(s);
			}

			int index = scores.size();
			for (Map.Entry<Integer, String> entry : scores.entrySet()) {
				Integer score = (entry.getKey()) != null ? entry.getKey() : index;
				objective.getScore(entry.getValue()).setScore(score);
				player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(entry.getValue()).setScore(score);
				--index;
			}
		}
	}

	/**
	 * Reset the Scoreboard to a pristine state
	 */
	public void resetScores() {
		if (player.getScoreboard() != null) {
			for (String s : player.getScoreboard().getEntries()) {
				player.getScoreboard().resetScores(s);
			}
		}
		for (String s : scoreboard.getEntries()) {
			scoreboard.resetScores(s);
		}
	}

	/**
	 * Clear the vars used to store the text
	 */
	public void clearScores() {
		scores.clear();
		teams.clear();
	}

	/**
	 * Get the displayName used on the Scoreboard
	 * @return displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set the display name to use
	 * @param displayName New display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Get Bukkit's Scoreboard used to represent this one
	 * @return scoreboard
	 */
	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	/**
	 * Get the Player who this Scoreboard is being show to
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}
}
