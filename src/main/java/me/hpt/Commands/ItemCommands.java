package me.hpt.Commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import me.hpt.ItemStorage.ItemInventory;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class ItemCommands {

	@Command(
			aliases = {"store"},
			desc = "Store your current inventory",
			usage = "[Internal name] [Display name]",
			min = 2,
			max = 2
	)
	@CommandPermissions("bossfight.item.store")
	public static void store(final CommandContext args, final CommandSender sender) throws CommandException {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Can only be a player to use this!");
			return;
		}

		Player user = (Player) sender;

		ItemInventory.createInventoryFromPlayerInventory(user, args.getString(0), args.getString(1));

		user.sendMessage(ChatColor.GREEN + "Created " + ChatColor.AQUA
				+ args.getString(0) + ChatColor.GREEN + " successfully.");
	}

	@Command(
			aliases = {"delete", "rm", "remove"},
			desc = "Delete an inventory from memory",
			usage = "[Internal name]",
			min = 1,
			max = 1
	)
	@CommandPermissions("bossfight.item.delete")
	public static void remove(final CommandContext args, final CommandSender sender) throws CommandException {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Can only be a player to use this!");
			return;
		}

		Player user = (Player) sender;

		if (ItemInventory.deleteInventory(args.getString(0))) {
			user.sendMessage(ChatColor.GREEN + "Successfully deleted " + args.getString(0));
		} else {
			user.sendMessage(ChatColor.RED + "Failed to delete " + args.getString(0));
		}
	}

	@Command(
			aliases = {"get", "rt"},
			desc = "Get an item inventory",
			usage = "[Internal name]",
			min = 1,
			max = 1
	)
	@CommandPermissions("bossfight.item.get")
	public static void get(final CommandContext args, final CommandSender sender) throws CommandException {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Can only be a player to use this!");
			return;
		}

		final Player user = (Player) sender;
		final String invName = args.getString(0);

		if (ItemInventory.containsInventory(invName)) {
			ItemInventory.getInventory(invName).setPlayerInventory(user);
			user.sendMessage(ChatColor.GREEN + "Success!");
		} else {
			user.sendMessage(ChatColor.DARK_RED + "Failed to find " + ChatColor.RED + invName);
		}
	}

	@Command(
			aliases = {"list", "ls"},
			desc = "List the available inventories",
			usage = "",
			min = 0,
			max = 0
	)
	@CommandPermissions("bossfight.item.list")
	public static void list(final CommandContext args, final CommandSender sender) throws CommandException {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Can only be a player to use this!");
			return;
		}

		final Player user = (Player) sender;

		user.sendMessage(ChatColor.GOLD + "Inventories: ");
		for (Map.Entry<String, ItemInventory> entry : ItemInventory.getInventories().entrySet()) {
			user.sendMessage(ChatColor.AQUA + entry.getKey());
		}
	}
}
