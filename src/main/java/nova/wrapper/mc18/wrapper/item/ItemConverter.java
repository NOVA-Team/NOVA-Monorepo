package nova.wrapper.mc18.wrapper.item;

import com.google.common.collect.HashBiMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import nova.core.block.BlockFactory;
import nova.core.component.Category;
import nova.core.item.Item;
import nova.core.item.ItemBlock;
import nova.core.item.ItemFactory;
import nova.core.item.ItemManager;
import nova.core.item.event.ItemIDNotFoundEvent;
import nova.core.loader.Loadable;
import nova.core.nativewrapper.NativeConverter;
import nova.core.retention.Data;
import nova.internal.core.Game;
import nova.internal.core.launch.InitializationException;
import nova.wrapper.mc18.launcher.NovaMinecraft;
import nova.wrapper.mc18.util.ModCreativeTab;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * The main class responsible for wrapping items.
 * @author Calclavia, Stan Hebben
 */
public class ItemConverter implements NativeConverter<Item, ItemStack>, Loadable {

	/**
	 * A map of all items registered
	 */
	private final HashBiMap<ItemFactory, MinecraftItemMapping> map = HashBiMap.create();

	public static ItemConverter instance() {
		return (ItemConverter) Game.natives().getNative(Item.class, ItemStack.class);
	}

	@Override
	public Class<Item> getNovaSide() {
		return Item.class;
	}

	@Override
	public Class<ItemStack> getNativeSide() {
		return ItemStack.class;
	}

	@Override
	public Item toNova(ItemStack itemStack) {
		return getNovaItem(itemStack).setCount(itemStack.stackSize);
	}

	//TODO: Why is this method separate?
	public Item getNovaItem(ItemStack itemStack) {
		if (itemStack.getItemDamage() == net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE) {
			// TODO: Deal with wildcard meta values - important for the ore dictionary
			return getNovaItem(new ItemStack(itemStack.getItem(), 1, 0));
		}

		if (itemStack.getTagCompound() != null && itemStack.getTagCompound() instanceof WrappedNBTTagCompound) {
			return ((WrappedNBTTagCompound) itemStack.getTagCompound()).getItem();
		} else {
			ItemFactory itemFactory = registerMinecraftMapping(itemStack.getItem(), itemStack.getItemDamage());

			Data data = itemStack.getTagCompound() != null ? Game.natives().toNova(itemStack.getTagCompound()) : new Data();
			if (!itemStack.getHasSubtypes() && itemStack.getItemDamage() > 0) {
				data.put("damage", itemStack.getItemDamage());
			}

			return itemFactory.makeItem(data);
		}
	}

	@Override
	public ItemStack toNative(Item item) {
		if (item == null) {
			return null;
		}

		//Prevent recusive wrapping
		if (item instanceof BWItem) {
			return ((BWItem) item).makeItemStack(item.count());
		} else {
			ItemFactory itemFactory = Game.items().getItem(item.getID()).get();
			WrappedNBTTagCompound tag = new WrappedNBTTagCompound(item);

			MinecraftItemMapping mapping = get(itemFactory);
			if (mapping == null) {
				throw new InitializationException("Missing mapping for " + itemFactory.getID());
			}

			ItemStack result = new ItemStack(mapping.item, item.count(), mapping.meta);
			result.setTagCompound(tag);
			return result;
		}
	}

	public ItemStack toNative(ItemFactory itemFactory) {
		WrappedNBTTagCompound tag = new WrappedNBTTagCompound(itemFactory.makeItem());

		MinecraftItemMapping mapping = get(itemFactory);
		if (mapping == null) {
			throw new InitializationException("Missing mapping for " + itemFactory.getID());
		}

		ItemStack result = new ItemStack(mapping.item, 1, mapping.meta);
		result.setTagCompound(tag);
		return result;
	}

	public ItemStack toNative(String id) {
		return toNative(Game.items().getItem(id).get().makeItem().setCount(1));
	}

	public MinecraftItemMapping get(ItemFactory item) {
		return map.get(item);
	}

	public ItemFactory get(MinecraftItemMapping minecraftItem) {
		return map.inverse().get(minecraftItem);
	}

	/**
	 * Saves NOVA item into a Minecraft ItemStack.
	 */
	public ItemStack updateMCItemStack(ItemStack itemStack, Item item) {
		itemStack.stackSize = item.count();
		itemStack.setTagCompound(Game.natives().toNative(item.factory().saveItem(item)));
		return itemStack;
	}

	/**
	 * Register all Nova blocks
	 */
	public void preInit() {
		registerNOVAItemsToMinecraft();
		registerMinecraftItemsToNOVA();
		registerSubtypeResolution();
	}

	private void registerNOVAItemsToMinecraft() {
		ItemManager itemManager = Game.items();

		//There should be no items registered during Native Converter preInit()
		//	item.registry.forEach(this::registerNOVAItem);
		itemManager.whenItemRegistered(this::onItemRegistered);
	}

	private void onItemRegistered(ItemManager.ItemRegistrationEvent event) {
		registerNOVAItem(event.itemFactory);
	}

	private void registerNOVAItem(ItemFactory itemFactory) {
		if (map.containsKey(itemFactory)) {
			// just a safeguard - don't map stuff twice
			return;
		}

		net.minecraft.item.Item itemWrapper;

		if (itemFactory.getDummy() instanceof ItemBlock) {
			BlockFactory blockFactory = ((ItemBlock) (itemFactory.getDummy())).blockFactory;
			net.minecraft.block.Block mcBlock = Game.natives().toNative(blockFactory.getDummy());
			itemWrapper = net.minecraft.item.Item.getItemFromBlock(mcBlock);
			if (itemWrapper == null) {
				throw new InitializationException("ItemConverter: Missing block: " + itemFactory.getID());
			}
		} else {
			itemWrapper = new FWItem(itemFactory);
		}

		MinecraftItemMapping minecraftItemMapping = new MinecraftItemMapping(itemWrapper, 0);
		map.put(itemFactory, minecraftItemMapping);

		// Don't register ItemBlocks twice
		if (!(itemFactory.getDummy() instanceof ItemBlock)) {
			NovaMinecraft.proxy.registerItem((FWItem) itemWrapper);
			GameRegistry.registerItem(itemWrapper, itemFactory.getID());

			if (itemFactory.getDummy().has(Category.class) && FMLCommonHandler.instance().getSide().isClient()) {
				//Add into creative tab
				Category category = itemFactory.getDummy().get(Category.class);
				Optional<CreativeTabs> first = Arrays.stream(CreativeTabs.creativeTabArray)
					.filter(tab -> tab.getTabLabel().equals(category.name))
					.findFirst();
				if (first.isPresent()) {
					itemWrapper.setCreativeTab(first.get());
				} else {
					ModCreativeTab tab = new ModCreativeTab(category.name, Game.natives().toNative(category.item));
					itemWrapper.setCreativeTab(tab);

					if (category.item == null) {
						tab.item = itemWrapper;
					}
				}
			}

			System.out.println("[NOVA]: Registered '" + itemFactory.getID() + "' item.");
		}
	}

	private void registerMinecraftItemsToNOVA() {
		Set<ResourceLocation> itemIDs = (Set<ResourceLocation>) net.minecraft.item.Item.itemRegistry.getKeys();
		itemIDs.forEach(itemID -> {
			net.minecraft.item.Item item = (net.minecraft.item.Item) net.minecraft.item.Item.itemRegistry.getObject(itemID);
			registerMinecraftMapping(item, 0);
		});
	}

	private void registerSubtypeResolution() {
		Game.items().whenIDNotFound(this::onIDNotFound);
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

			net.minecraft.item.Item item = (net.minecraft.item.Item) net.minecraft.item.Item.itemRegistry.getObject(itemID);
			if (item == null || !item.getHasSubtypes()) {
				return;
			}

			event.setRemappedFactory(registerMinecraftMapping(item, meta));
		} catch (NumberFormatException ex) {
		}
	}

	private ItemFactory registerMinecraftMapping(net.minecraft.item.Item item, int meta) {
		MinecraftItemMapping mapping = new MinecraftItemMapping(item, meta);
		if (map.inverse().containsKey(mapping)) {
			// don't register twice, return the factory instead
			return map.inverse().get(mapping);
		}

		MCItemFactory itemFactory = new MCItemFactory(item, meta);
		map.put(itemFactory, mapping);

		Game.items().register(itemFactory);

		return itemFactory;
	}

	/**
	 * Used to map MC items and their meta to nova item factories.
	 */
	public final class MinecraftItemMapping {
		public final net.minecraft.item.Item item;
		public final int meta;

		public MinecraftItemMapping(net.minecraft.item.Item item, int meta) {
			this.item = item;
			this.meta = item.getHasSubtypes() ? meta : 0;
		}

		public MinecraftItemMapping(ItemStack itemStack) {
			this.item = itemStack.getItem();
			this.meta = itemStack.getHasSubtypes() ? itemStack.getItemDamage() : 0;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			MinecraftItemMapping that = (MinecraftItemMapping) o;

			if (meta != that.meta) {
				return false;
			}
			if (!item.equals(that.item)) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = item.hashCode();
			result = 31 * result + meta;
			return result;
		}

		@Override
		public String toString() {
			if (item.getHasSubtypes()) {
				return net.minecraft.item.Item.itemRegistry.getNameForObject(item) + ":" + meta;
			} else {
				return net.minecraft.item.Item.itemRegistry.getNameForObject(item).toString();
			}
		}
	}
}
