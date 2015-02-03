package nova.wrapper.mc1710.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import nova.core.game.Game;
import nova.core.item.ItemBlock;
import nova.core.item.ItemFactory;
import nova.core.item.ItemManager;
import nova.core.item.event.ItemIDNotFoundEvent;
import nova.wrapper.mc1710.launcher.NovaMinecraft;
import nova.wrapper.mc1710.util.NBTUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Calclavia, Stan Hebben
 */
public class ItemWrapperRegistry {

	public static final ItemWrapperRegistry instance = new ItemWrapperRegistry();

	/**
	 * A map of all blocks registered
	 */
    // TODO: can we use a BiMap for this?
	private final Map<ItemFactory, MinecraftItemMapping> forwardMap = new HashMap<>();
    private final Map<MinecraftItemMapping, ItemFactory> backwardMap = new HashMap<>();

	public MinecraftItemMapping get(ItemFactory item) {
		return forwardMap.get(item);
	}

    public ItemFactory get(MinecraftItemMapping minecraftItem) {
        return backwardMap.get(minecraftItem);
    }

    public net.minecraft.item.ItemStack getMCItemStack(nova.core.item.ItemStack itemStack) {
        if (itemStack == null)
            return null;

        if (itemStack.getItem() instanceof MCItem) {
            return ((MCItem) itemStack.getItem()).makeItemStack(itemStack.getStackSize());
        } else {
            ItemFactory itemFactory = Game.instance.get().itemManager.getItemFactory(itemStack.getItem().getID()).get();
            LinkedNBTTagCompound tag = new LinkedNBTTagCompound(itemStack.getItem());

            MinecraftItemMapping mapping = get(itemFactory);
            ItemStack result = new ItemStack(mapping.item, mapping.meta, itemStack.getStackSize());
            result.setTagCompound(tag);
            return result;
        }
    }

    public nova.core.item.ItemStack getNovaItemStack(net.minecraft.item.ItemStack itemStack) {
        if (itemStack.getTagCompound() != null && itemStack.getTagCompound() instanceof LinkedNBTTagCompound) {
            return new nova.core.item.ItemStack(
                    ((LinkedNBTTagCompound) itemStack.getTagCompound()).getItem(),
                    itemStack.stackSize);
        } else {
            MinecraftItemMapping mapping = new MinecraftItemMapping(itemStack);
            ItemFactory itemFactory = backwardMap.get(mapping);
            if (itemFactory == null && itemStack.getHasSubtypes())
                // load subitem
                itemFactory = registerMinecraftMapping(itemStack.getItem(), itemStack.getItemDamage());

            Map<String, Object> data = NBTUtility.nbtToMap(itemStack.getTagCompound());
            if (!itemStack.getHasSubtypes() && itemStack.getItemDamage() > 0) {
                if (data == null)
                    data = new HashMap<>();

                data.put("damage", itemStack.getItemDamage());
            }

            nova.core.item.Item item = itemFactory.makeItem(data);
            return new nova.core.item.ItemStack(item, itemStack.stackSize);
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
        if (backwardMap.containsKey(itemFactory))
            // just a safeguard - don't map stuff twice
            return;

        if (itemFactory.getDummy() instanceof ItemBlock)
            // don't register ItemBlocks twice
            return;

        ItemWrapper itemWrapper = new ItemWrapper(itemFactory);

        MinecraftItemMapping minecraftItemMapping = new MinecraftItemMapping(itemWrapper, 0);
        forwardMap.put(itemFactory, minecraftItemMapping);
        backwardMap.put(minecraftItemMapping, itemFactory);

        NovaMinecraft.proxy.registerItem(itemWrapper);
        GameRegistry.registerItem(itemWrapper, itemFactory.getID());

        //TODO: Testing purposes:
        itemWrapper.setCreativeTab(CreativeTabs.tabBlock);
        System.out.println("[NOVA]: Registered '" + itemFactory.getID() + "' item.");
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
        if (lastColon < 0)
            return;

        try {
            int meta = Integer.parseInt(event.id.substring(lastColon + 1));
            String itemID = event.id.substring(0, lastColon);

            Item item = (Item) Item.itemRegistry.getObject(itemID);
            if (item == null || !item.getHasSubtypes())
                return;

            event.setRemappedFactory(registerMinecraftMapping(item, meta));
        } catch (NumberFormatException ex) {}
    }

    private ItemFactory registerMinecraftMapping(Item item, int meta) {
        MinecraftItemMapping mapping = new MinecraftItemMapping(item, meta);
        if (backwardMap.containsKey(mapping))
            // don't register twice, return the factory instead
            return backwardMap.get(mapping);

        MCItemFactory itemFactory = new MCItemFactory(item, meta);
        forwardMap.put(itemFactory, mapping);
        backwardMap.put(mapping, itemFactory);

        return itemFactory;
    }
}
