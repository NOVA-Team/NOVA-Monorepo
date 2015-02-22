package nova.wrapper.mc1710.item;

import com.google.common.collect.HashBiMap;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import nova.core.block.Block;
import nova.core.game.Game;
import nova.core.item.ItemBlock;
import nova.core.item.ItemFactory;
import nova.core.item.ItemManager;
import nova.core.item.event.ItemIDNotFoundEvent;
import nova.core.util.Category;
import nova.core.util.exception.NovaException;
import nova.wrapper.mc1710.forward.block.BlockWrapperRegistry;
import nova.wrapper.mc1710.launcher.NovaMinecraft;
import nova.wrapper.mc1710.util.ModCreativeTab;
import nova.wrapper.mc1710.util.NBTUtility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Calclavia, Stan Hebben
 */
public class ItemWrapperRegistry {

	public static final ItemWrapperRegistry instance = new ItemWrapperRegistry();

	/**
	 * A map of all blocks registered
	 */
	private final HashBiMap<ItemFactory, MinecraftItemMapping> map = HashBiMap.create();

	public MinecraftItemMapping get(ItemFactory item) {
		return map.get(item);
	}

	public ItemFactory get(MinecraftItemMapping minecraftItem) {
		return map.inverse().get(minecraftItem);
	}

	public net.minecraft.item.ItemStack getMCItemStack(nova.core.item.Item item) {
		if (item == null) {
			return null;
		}

		if (item instanceof MCItem) {
			return ((MCItem) item).makeItemStack(item.count());
		} else {
			ItemFactory itemFactory = Game.instance.get().itemManager.getItemFactory(item.getID()).get();
			LinkedNBTTagCompound tag = new LinkedNBTTagCompound(item);

			MinecraftItemMapping mapping = get(itemFactory);
			if (mapping == null) {
				throw new NovaException("Missing mapping for " + itemFactory.getID());
			}

			ItemStack result = new ItemStack(mapping.item, item.count(), mapping.meta);
			result.setTagCompound(tag);
			return result;
		}
	}

	/**
	 * Saves NOVA item into a Minecraft ItemStack.
	 */
	public net.minecraft.item.ItemStack updateMCItemStack(ItemStack itemStack, nova.core.item.Item item) {
		itemStack.stackSize = item.count();
		itemStack.setTagCompound(NBTUtility.mapToNBT(item.factory().saveItem(item)));
		return itemStack;
	}

	public net.minecraft.item.ItemStack getMCItemStack(String id) {
		return getMCItemStack(Game.instance.get().itemManager.getItemFactory(id).get().makeItem().setCount(1));
	}

	public nova.core.item.Item getNovaItemStack(net.minecraft.item.ItemStack itemStack) {
		return getNovaItem(itemStack).setCount(itemStack.stackSize);
	}

	public nova.core.item.Item getNovaItem(net.minecraft.item.ItemStack itemStack) {
		if (itemStack.getItemDamage() == net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE) {
			// TODO: Deal with wildcard meta values - important for the ore dictionary
			return getNovaItem(new ItemStack(itemStack.getItem(), 1, 0));
		}

		if (itemStack.getTagCompound() != null && itemStack.getTagCompound() instanceof LinkedNBTTagCompound) {
			return ((LinkedNBTTagCompound) itemStack.getTagCompound()).getItem();
		} else {
			MinecraftItemMapping mapping = new MinecraftItemMapping(itemStack);
			ItemFactory itemFactory = map.inverse().get(mapping);
			if (itemFactory == null && itemStack.getHasSubtypes())
			// load subitem
			{
				itemFactory = registerMinecraftMapping(itemStack.getItem(), itemStack.getItemDamage());
			}

			Map<String, Object> data = NBTUtility.nbtToMap(itemStack.getTagCompound());
			if (!itemStack.getHasSubtypes() && itemStack.getItemDamage() > 0) {
				if (data == null) {
					data = new HashMap<>();
				}

				data.put("damage", itemStack.getItemDamage());
			}

			return itemFactory.makeItem(data);
		}
	}

	/**
	 * Register all Nova blocks
	 */
	public void registerItems() {
		registerNOVAItemsToMinecraft();
		registerMinecraftItemsToNOVA();
		registerSubtypeResolution();
	}

	private void registerNOVAItemsToMinecraft() {
		ItemManager itemManager = Game.instance.get().itemManager;

		itemManager.registry.forEach(this::registerNOVAItem);
		itemManager.whenItemRegistered(this::onItemRegistered);
	}

	private void onItemRegistered(ItemManager.ItemRegistrationEvent event) {
		registerNOVAItem(event.itemFactory);
	}

	private void registerNOVAItem(ItemFactory itemFactory) {
		if (map.containsKey(itemFactory))
		// just a safeguard - don't map stuff twice
		{
			return;
		}

		net.minecraft.item.Item itemWrapper;

		if (itemFactory.getDummy() instanceof ItemBlock) {
			Block block = ((ItemBlock) (itemFactory.getDummy())).block;
			net.minecraft.block.Block mcBlock = BlockWrapperRegistry.instance.getMCBlock(block);
			itemWrapper = Item.getItemFromBlock(mcBlock);
			if (itemWrapper == null) {
				throw new NovaException("Missing block: " + itemFactory.getID());
			}
		} else {
			itemWrapper = new FWItem(itemFactory);
		}

		MinecraftItemMapping minecraftItemMapping = new MinecraftItemMapping(itemWrapper, 0);
		map.put(itemFactory, minecraftItemMapping);

		// don't register ItemBlocks twice
		if (!(itemFactory.getDummy() instanceof ItemBlock)) {
			NovaMinecraft.proxy.registerItem((FWItem) itemWrapper);
			GameRegistry.registerItem(itemWrapper, itemFactory.getID());

			if (((FWItem) itemWrapper).itemFactory.getDummy() instanceof Category) {
				//Add into creative tab
				Category category = (Category) ((FWItem) itemWrapper).itemFactory.getDummy();
				Optional<CreativeTabs> first = Arrays.stream(CreativeTabs.creativeTabArray)
					.filter(tab -> tab.getTabLabel().equals(category.getCategory()))
					.findFirst();
				if (first.isPresent()) {
					itemWrapper.setCreativeTab(first.get());
				} else {
					ModCreativeTab tab = new ModCreativeTab(category.getCategory());
					itemWrapper.setCreativeTab(tab);
					tab.item = itemWrapper;
				}
			}
			System.out.println("[NOVA]: Registered '" + itemFactory.getID() + "' item.");
		}
	}

	private void registerMinecraftItemsToNOVA() {
		Set<String> itemIDs = (Set<String>) Item.itemRegistry.getKeys();
		itemIDs.forEach(itemID -> {
			Item item = (Item) Item.itemRegistry.getObject(itemID);
			registerMinecraftMapping(item, 0);
		});
	}

	private void registerSubtypeResolution() {
		Game.instance.get().itemManager.whenIDNotFound(this::onIDNotFound);
	}

	private void onIDNotFound(ItemIDNotFoundEvent event) {
		// if item minecraft:planks:2 is detected, this code will register minecraft:planks:2 dynamically
		// we cannot do this up front since there is **NO** reliable way to get the sub-items of an item

		int lastColon = event.id.lastIndexOf(':');
		if (lastColon < 0) {
			return;
		}

		try {
			int meta = Integer.parseInt(event.id.substring(lastColon + 1));
			String itemID = event.id.substring(0, lastColon);

			Item item = (Item) Item.itemRegistry.getObject(itemID);
			if (item == null || !item.getHasSubtypes()) {
				return;
			}

			event.setRemappedFactory(registerMinecraftMapping(item, meta));
		} catch (NumberFormatException ex) {
		}
	}

	private ItemFactory registerMinecraftMapping(Item item, int meta) {
		MinecraftItemMapping mapping = new MinecraftItemMapping(item, meta);
		if (map.inverse().containsKey(mapping))
		// don't register twice, return the factory instead
		{
			return map.inverse().get(mapping);
		}

		MCItemFactory itemFactory = new MCItemFactory(item, meta);
		map.put(itemFactory, mapping);

		Game.instance.get().itemManager.register(itemFactory);

		return itemFactory;
	}

}
