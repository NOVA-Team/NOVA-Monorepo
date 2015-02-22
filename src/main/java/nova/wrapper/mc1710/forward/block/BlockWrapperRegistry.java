package nova.wrapper.mc1710.forward.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.game.Game;
import nova.core.util.Category;
import nova.wrapper.mc1710.item.FWItemBlock;
import nova.wrapper.mc1710.launcher.NovaMinecraft;
import nova.wrapper.mc1710.util.ModCreativeTab;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Calclavia
 */
public class BlockWrapperRegistry {
	public static final BlockWrapperRegistry instance = new BlockWrapperRegistry();

	/**
	 * A map of all blocks registered
	 */
	public final HashMap<Block, net.minecraft.block.Block> blockWrapperMap = new HashMap<>();

	/**
	 * Register all Nova blocks
	 */
	public void registerBlocks() {
		BlockManager blockManager = Game.instance.get().blockManager;

		blockManager.registry.forEach(this::addNOVABlock);
		blockManager.whenBlockRegistered(this::onBlockRegistered);
	}

	public net.minecraft.block.Block getMCBlock(Block novaBlock) {
		return blockWrapperMap.get(novaBlock);
	}

	private void onBlockRegistered(BlockManager.BlockRegisteredEvent event) {
		addNOVABlock(event.blockFactory);
	}

	private void addNOVABlock(BlockFactory blockFactory) {
		FWBlock blockWrapper = new FWBlock(blockFactory);
		blockWrapperMap.put(blockWrapper.block, blockWrapper);
		NovaMinecraft.proxy.registerBlock(blockWrapper);
		GameRegistry.registerBlock(blockWrapper, FWItemBlock.class, blockFactory.getID());

		if (blockWrapper.block instanceof Category) {
			//Add into creative tab
			Category category = (Category) blockWrapper.block;
			Optional<CreativeTabs> first = Arrays.stream(CreativeTabs.creativeTabArray)
				.filter(tab -> tab.getTabLabel().equals(category.getCategory()))
				.findFirst();
			if (first.isPresent()) {
				blockWrapper.setCreativeTab(first.get());
			} else {
				ModCreativeTab tab = new ModCreativeTab(category.getCategory());
				blockWrapper.setCreativeTab(tab);
				tab.item = Item.getItemFromBlock(blockWrapper);
			}
		}

		System.out.println("[NOVA]: Registered '" + blockFactory.getID() + "' block.");
	}
}
