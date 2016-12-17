package me.hpt.Commands;

import com.sk89q.minecraft.util.commands.*;
import org.bukkit.command.CommandSender;

public class ItemParentCommand {
	@Command(
			aliases = {"bossitem", "bi", "bossi"},
			desc = "Boss Item parent command",
			usage = "/bossitem help"
	)
	@NestedCommand(ItemCommands.class)
	@CommandPermissions("bossfight.item.parent")
	public static void itemParentCommand(final CommandContext args, final CommandSender sender) throws CommandException {

	}
}
