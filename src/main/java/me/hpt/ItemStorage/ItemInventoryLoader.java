package me.hpt.ItemStorage;

import me.hpt.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ItemInventoryLoader {

	// Where all the inventory in the config are located under
	private static final String INV_KEY = "invs";

	private static final String NAME_KEY = "name"; // Display name of the ItemInventory

	// Keys where the armor pieces are set
	private static final String HELMET_KEY = "helmet";
	private static final String CHESTPLATE_KEY = "chestplate";
	private static final String LEGGINGS_KEY = "leggings";
	private static final String BOOTS_KEY = "boots";

	// Where the items are located
	private static final String ITEMS_KEY = "items";

	// What slot items go into. This is found under ITEMS_KEY
	private static final String ITEMS_SLOT_KEY = "slot";

	// What ItemStack is at the slot found at ITEMS_SLOT_KEY. This is found under ITEMS_KEY
	private static final String ITEMS_ITEM_KEY = "item";

	/**
	 * Load an ItemInventory from a config file
	 * @param section Section of the config to load from
	 * @return The ItemInventory found at the section given
	 */
	public static ItemInventory loadInv(ConfigurationSection section) {
		// Load the display name
		String displayName = "&4NOT SET"; // &4 - Red
		if (section.isSet(NAME_KEY) && section.isString(NAME_KEY)) {
			displayName = section.getString(NAME_KEY);
		}

		ItemInventory inv = new ItemInventory(displayName);

		/*
		// Load the armor pieces. If the key isn't found null is returned, which is fine
		inv.setHelmet(section.getItemStack(HELMET_KEY));
		inv.setChestplate(section.getItemStack(CHESTPLATE_KEY));
		inv.setLeggings(section.getItemStack(LEGGINGS_KEY));
		inv.setBoots(section.getItemStack(BOOTS_KEY));
		*/

		// Section where all the items are stored at
		ConfigurationSection itemSection = section.getConfigurationSection(ITEMS_KEY);

		// No items have been stored
		if (itemSection == null) {
			Logger.warn("itemSection not found");
			return inv;
		}

		// Iterate thru each key to find the ItemStack they create
		// What slot the item goes into is located at key.ITEMS_SLOT_KEY
		// What the item is, is found at key.ITEM_ITEM_KEY
		for (String s : itemSection.getKeys(false)) {
			Logger.info("key: %s", s);
			inv.setItem(itemSection.getItemStack(s + '.' + ITEMS_ITEM_KEY),
					itemSection.getInt(s + '.' + ITEMS_SLOT_KEY));
		}

		return inv;
	}

	/**
	 * Load all the ItemInventories found in a config file under the key INV_KEY
	 * @param config Config file to load from
	 * @return A Map of all the ItemInventories found, with the key being the iternal name of the ItemInventory
	 */
	public static Map<String, ItemInventory> loadInventories(FileConfiguration config) {
		HashMap<String, ItemInventory> invs = new HashMap<>(); // Map to load the ItemInventories into

		ConfigurationSection invSection = config.getConfigurationSection(INV_KEY);
		Logger.info("Loading %d inventories", invSection.getKeys(false).size());
		for (String s : invSection.getKeys(false)) {
			invs.put(s, loadInv(invSection.getConfigurationSection(s)));
		}

		return invs;
	}

	public static void saveInventories(FileConfiguration config, Map<String, ItemInventory> invs) {
		ConfigurationSection invSection;
		if (config.contains(INV_KEY) && config.isConfigurationSection(INV_KEY)) {
			invSection = config.getConfigurationSection(INV_KEY);
		} else {
			invSection = config.createSection(INV_KEY);
		}

		for (Map.Entry<String, ItemInventory> entry : invs.entrySet()) {
			// Set up the configSection that will store the ItemInventory
			if (invSection.contains(entry.getKey())) {
				if (!invSection.isConfigurationSection(entry.getKey())) {
					Logger.error("%s.%s is set, but not a configSection.", INV_KEY, entry.getKey());
				}
			} else {
				invSection.createSection(entry.getKey());
			}

			// Section used to store the ItemInventory in the Entry
			ConfigurationSection section = invSection.getConfigurationSection(entry.getKey());

			section.set(NAME_KEY, entry.getValue().getDisplayName());

			/*
			section.set(HELMET_KEY, entry.getValue().getHelmet());
			section.set(CHESTPLATE_KEY, entry.getValue().getChestplate());
			section.set(LEGGINGS_KEY, entry.getValue().getLeggings());
			section.set(BOOTS_KEY, entry.getValue().getBoots());
			*/

			ConfigurationSection itemSection = section.createSection(ITEMS_KEY);

			// Save the items in the ItemInventory
			int index = 0;
			for (int i = 0; i < entry.getValue().getItems().size(); ++i) {
				if (entry.getValue().getItems().get(i) != ItemInventory.EMPTY_ITEM) {
					ConfigurationSection itemSubSection = itemSection.createSection(String.valueOf(index++));
					itemSubSection.set(ITEMS_SLOT_KEY, i);
					itemSubSection.set(ITEMS_ITEM_KEY, entry.getValue().getItems().get(i));
				}
			}
		}
	}
}
