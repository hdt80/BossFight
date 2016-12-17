package me.hpt.Commands;

import com.sk89q.minecraft.util.commands.*;
import org.bukkit.command.CommandSender;

public class BossParentCommand {

	@Command(
			aliases = {"boss"},
			desc = "Boss Fight parent command",
			usage = "/boss help"
	)
	@NestedCommand(BossCommands.class)
	@CommandPermissions("bossfight.fight.parent")
	public static void bossParentCommand(final CommandContext args, final CommandSender sender) throws CommandException {

	}
}
