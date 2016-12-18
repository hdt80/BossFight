package me.hpt;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import me.hpt.Commands.BossParentCommand;
import me.hpt.Commands.ItemParentCommand;
import me.hpt.ItemStorage.ItemInventory;
import me.hpt.ItemStorage.ItemInventoryLoader;
import me.hpt.Listeners.BossHitListener;
import me.hpt.Listeners.PlayerLifeListener;
import me.hpt.Listeners.PlayerTrackingListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BossFight extends JavaPlugin {

	private static BossFight instance;

	private CommandsManager<CommandSender> commands;

	@Override
	public void onEnable() {
		instance = this;

		Logger.setLogger(getLogger());
		registerCommands();

		Config.load(this);

		Messages.load(this, "Messages.lang");

		ItemInventory.setInventories(ItemInventoryLoader.loadInventories(Config.get()));

		new BossHitListener(this);
		new PlayerTrackingListener(this);
		new PlayerLifeListener(this);
	}

	@Override
	public void onDisable() {

		// Remove all BossBars
		BossBar.clearAll();

		// Save and override all the stored ItemInventories
		ItemInventoryLoader.saveInventories(Config.get(), ItemInventory.getInventories());

		// Save the config file
		Config.save();
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
		cmdRegister.register(ItemParentCommand.class);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		try {
			commands.execute(command.getName(), args, sender, sender);
			return true;
		} catch (CommandPermissionsException e) {
			sender.sendMessage(Messages.get("Command-InvalidPermissions"));
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
				sender.sendMessage(Messages.get("Command-ExpectedNumber"));
			} else {
				sender.sendMessage(Messages.get("Command-UnhandledException"));
				e.printStackTrace();
			}
			return false;
		} catch (CommandException e) {
			sender.sendMessage(e.getMessage());
			return false;
		}
	}
}
