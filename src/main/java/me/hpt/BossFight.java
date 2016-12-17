package me.hpt;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import me.hpt.Commands.BossParentCommand;
import me.hpt.Listeners.BossHitListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BossFight extends JavaPlugin {

	private static BossFight instance;

	private CommandsManager<CommandSender> commands;

	@Override
	public void onEnable() {
		instance = this;
		Logger.setLogger(getLogger());
		registerCommands();

		new BossHitListener(this);

		Logger.broadcast("Enabled!");
	}

	@Override
	public void onDisable() {
		BossBar bar = null;
		for (Player p : getServer().getOnlinePlayers()) {
			bar = BossBar.getBossBar(p);

			if (bar != null) {
				bar.removePlayer(p);
			}
		}
	}

	public static BossFight get() {
		return instance;
	}

	/**
	 * Register all commands
	 */
	private void registerCommands() {
		commands = new CommandsManager<CommandSender>() {
			@Override
			public boolean hasPermission(CommandSender commandSender, String s) {
				return (commandSender instanceof Console)
						|| (commandSender.hasPermission(s))
						|| (commandSender.hasPermission("*"))
						|| (commandSender.isOp())
						|| (commandSender.hasPermission("bossbar.*"));
			}
		};

		CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, commands);

		cmdRegister.register(BossParentCommand.class);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		try {
			commands.execute(command.getName(), args, sender, sender);
			return true;
		} catch (CommandPermissionsException e) {
			sender.sendMessage("You do not have permission to use this command!");
			return false;
		} catch (MissingNestedCommandException e) {
			sender.sendMessage(e.getUsage());
			return false;
		} catch (CommandUsageException e) {
			sender.sendMessage(e.getMessage());
			sender.sendMessage(e.getUsage());
			return false;
		} catch (WrappedCommandException e) {
			if (e.getCause() instanceof NumberFormatException) {
				sender.sendMessage("Wrong argument type. Please supply a number");
			} else {
				sender.sendMessage("An error has occurred. Please use /help to notify a staff member");
				e.printStackTrace();
			}
			return false;
		} catch (CommandException e) {
			sender.sendMessage(e.getMessage());
			return false;
		}
	}
}
