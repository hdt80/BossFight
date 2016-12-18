package me.hpt.Commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import me.hpt.BossFightManager;
import me.hpt.Messages;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BossCommands {

	@Command(
			aliases = {"start"},
			desc = "Start the Boss Bar",
			usage = "[Health]",
			min = 1,
			max = 1
	)
	@CommandPermissions("bossfight.fight.start")
	public static void startBossBar(final CommandContext args, final CommandSender sender) throws CommandException {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Messages.get("Command-PlayersOnly"));
			return;
		}

		Player user = (Player) sender;
		int health = args.getInteger(0);

		if (health < 1) {
			sender.sendMessage(Messages.get("Command-HealthGreaterThanOne"));
			return;
		}

		BossFightManager.startFight(user, health);
	}

	@Command(
			aliases = {"end", "stop"},
			desc = "End the Boss Bar",
			usage = "",
			min = 0,
			max = 0
	)
	@CommandPermissions("bossfight.fight.end")
	public static void endBossBar(final CommandContext args, final CommandSender sender) throws CommandException {
		BossFightManager.endFight(true);
	}

	@Command(
			aliases = {"inc"},
			desc = "Increase the Boss Bar by one",
			usage = "",
			min = 0,
			max = 0
	)
	@CommandPermissions("bossfight.fight.inc")
	public static void incBossBar(final CommandContext args, final CommandSender sender) throws CommandException {
		BossFightManager.incCounter();
	}

	@Command(
			aliases = {"dec"},
			desc = "Increase the Boss Bar by one",
			usage= "",
			min = 0,
			max = 0
	)
	@CommandPermissions("bossfight.fight.dec")
	public static void decBossBar(final CommandContext args, final CommandSender sender) throws CommandException {
		BossFightManager.decCounter();
	}

	@Command(
			aliases = {"fly"},
			desc = "Increase the Boss Bar by one",
			usage = "",
			min = 0,
			max = 0
	)
	@CommandPermissions("bossfight.fight.fly")
	public static void fly(final CommandContext args, final CommandSender sender) throws CommandException {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Messages.get("Command-PlayersOnly"));
			return;
		}

		Player user = (Player) sender;

		user.setAllowFlight(!user.getAllowFlight());

		user.sendMessage(Messages.get("Command-BossFly", user.getAllowFlight()));
	}

	@Command(
			aliases = {"spawn", "respawn"},
			desc = "Set the spawnpoint of the fight",
			usage = "",
			min = 0,
			max = 0
	)
	@CommandPermissions("bossfight.fight.spawn")
	public static void setRespawnLoc(final CommandContext args, final CommandSender sender) throws CommandException {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be used by players");
			return;
		}

		BossFightManager.setRespawnLocation(((Player) sender).getLocation());

		Location loc = BossFightManager.getRespawnLocation();

		sender.sendMessage("Spawn set: (" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
	}
}
