package me.hpt.ItemStorage;

import me.hpt.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemInventory {

	private String displayName; // Name to display to the users

	private List<ItemStack> items; // Items stored in this ItemList

	// Armor pieces
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;

	static Map<String, ItemInventory> inventories; // What internalNames match to what ItemInventory

	public static final ItemStack EMPTY_ITEM;

	static {
		inventories = new HashMap<>();
		EMPTY_ITEM = new ItemStack(Material.AIR);
	}

	/**
	 * Get the ItemInventory associated with a name
	 * @param name Internal name of the ItemInventory to get
	 * @return The ItemInventory with the same internal name as name, or null of not found
	 */
	public static ItemInventory getInventory(String name) {
		if (inventories.containsKey(name)) {
			return inventories.get(name);
		}
		return null;
	}

	/**
	 * Set the inventories stored
	 * @param invs Inventories to store
	 */
	public static void setInventories(Map<String, ItemInventory> invs) {
		inventories = invs;
	}

	/**
	 * Get all the stored inventories
	 * @return inventories
	 */
	public static Map<String, ItemInventory> getInventories() {
		return inventories;
	}

	/**
	 * Check if an ItemInventory is stored under an internal name
	 * @param name Internal name to check
	 * @return If inventories contains the internal name name
	 */
	public static boolean containsInventory(String name) {
		return inventories.containsKey(name);
	}

	/**
	 * Delete an ItemInventory
	 * @param name Internal name of the ItemInventory
	 * @return If the operation was successful
	 */
	public static boolean deleteInventory(String name) {
		return (inventories.remove(name) == null);
	}

	/**
	 * Create a new ItemInventory
	 * @param internalName Internal name to refer to the ItemInventory by
	 * @param displayName Name to display the ItemInventory as
	 * @return The created ItemInventory, or if the internalName is in use, the ItemInventory mapped to the name
	 */
	public static ItemInventory createInventory(String internalName, String displayName) {
		if (containsInventory(internalName)) {
			return getInventory(internalName);
		}

		ItemInventory inv = new ItemInventory(displayName);

		inventories.put(internalName, inv);

		return inv;
	}

	/**
	 * Create an ItemInventory from the contents of a Player's inventory
	 * @param player Player whose inventory will be stored as an ItemInventory
	 * @param internalName Interal name to refer to the ItemInventory by
	 * @param displayName NAme to display the ItemInventory as
	 * @return The created ItemInventory, of if the internalName is in us, the ItemInventory mapp to the name
	 */
	public static ItemInventory createInventoryFromPlayerInventory(Player player, String internalName, String displayName) {
		if (containsInventory(internalName)) {
			Logger.warn("Inventory %s already created.", internalName);
			return getInventory(internalName);
		}

		ItemInventory inv = createInventory(internalName, displayName);

		inv.setHelmet(player.getInventory().getHelmet());
		inv.setChestplate(player.getInventory().getChestplate());
		inv.setLeggings(player.getInventory().getLeggings());
		inv.setBoots(player.getInventory().getBoots());

		// Only get the first 36 items, the rest are armor and such
		for (int i = 0; i < 36; ++i) {
			inv.setItem(player.getInventory().getContents()[i], i);
		}

		return inv;
	}

	/**
	 * Ctor
	 * @param displayName Display name to show the users. If set to null the displayName will be the same as the internalName
	 */
	ItemInventory(String displayName) {

		this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);

		items = new ArrayList<>(36); // 36 slots in a Player's inventory

		// Add 36 items to make sure the inventory is the right size
		for (int i = 0; i < 36; ++i) {
			items.add(EMPTY_ITEM);
		}
		Logger.debug("items size: %d", items.size());

		helmet = null;
		chestplate = null;
		leggings = null;
		boots = null;
	}

	/**
	 * Set a Player's inventory to what items are in this inventory
	 * @param player What Player to set the inventory to
	 */
	public void setPlayerInventory(Player player) {
		for (int i = 0; i < items.size(); ++i) {
			player.getInventory().setItem(i, items.get(i));
		}

		// Set the player's armor
		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setLeggings(leggings);
		player.getInventory().setBoots(boots);
	}

	/**
	 * Add a new ItemStack to the ItemList
	 * @param item ItemStack to add. This will only be added if the item is not already in the inventory
	 */
	public void addItem(ItemStack item) {
		// Find the next open slot in the inventory
		int index = 0;
		for (; index < 36; ++index) {
			if (items.get(index) == null) {
				if (index == 35) {
					index = -1;
				}
				break;
			}
		}

		Validate.isTrue(index == -1, "No open inventory slot found!");

		setItem(item, index);
	}

	/**
	 * Set a specific index of the inventory to an item. Any item in the index will be overridden
	 * @param item Item to set
	 * @param slot Index to set to. Must between 0 and 35, 0 being the left most slot on the hotbar.
	 */
	public void setItem(ItemStack item, int slot) {
		Validate.isTrue((slot >= 0 && slot < 36), "Slot " + slot + " is out of range!");

		items.set(slot, item);
	}

	/**
	 * Remove an item from the ItemList
	 * @param item ItemStack to remove from the list
	 */
	public void removeItem(ItemStack item) {
		int index = items.indexOf(item);
		if (index >= 0) {
			items.remove(index);
		}
	}

	/**
	 * Check if this ItemList has ths item
	 * @param item Item to check for
	 * @return If items contains the item
	 */
	public boolean hasItem(ItemStack item) {
		return items.contains(item);
	}

	/**
	 * Get the display name used by this ItemInventory
	 * @return displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Get the items used in this ItemInventory
	 * @return items
	 */
	public List<ItemStack> getItems() {
		return items;
	}

	/**
	 * Set the item used for the helmet
	 * @param helmet ItemStack to get the helmet to
	 */
	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	/**
	 * Set the item used for the chestplate
	 * @param chestplate ItemStack to get the chestplate to
	 */
	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}

	/**
	 * Set the item used for the leggings
	 * @param leggings ItemStack to get the leggings to
	 */
	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
	}

	/**
	 * Set the item used for the boots
	 * @param boots ItemStack to get the boots to
	 */
	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}

	/**
	 * Get the helmet used by this inventory. Can be null.
	 * @return helmet
	 */
	public ItemStack getHelmet() {
		return helmet;
	}

	/**
	 * Get the chestplate used by this inventory. Can be null.
	 * @return chestplate
	 */
	public ItemStack getChestplate() {
		return chestplate;
	}

	/**
	 * Get the leggings used by this inventory. Can be null.
	 * @return leggings
	 */
	public ItemStack getLeggings() {
		return leggings;
	}

	/**
	 * Get the boots used by this inventory. Can be null.
	 * @return boots
	 */
	public ItemStack getBoots() {
		return boots;
	}

	/**
	 * Clear out the inventory
	 */
	public void clear() {
		for (int i = 0; i < items.size(); ++i) {
			items.set(i, new ItemStack(Material.AIR));
		}

		helmet = null;
		chestplate = null;
		leggings = null;
		boots = null;
	}
}
