package nova.wrapper.mc1710.forward.block;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.game.Game;
import nova.core.util.Category;
import nova.wrapper.mc1710.backward.block.BWBlock;
import nova.wrapper.mc1710.wrapper.item.FWItemBlock;
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
	 * A map of all blockFactory to MC blocks registered
	 */
	public final HashMap<BlockFactory, net.minecraft.block.Block> blockFactoryMap = new HashMap<>();

	/**
	 * Register all Nova blocks
	 */
	public void registerBlocks() {
		BlockManager blockManager = Game.instance.blockManager;

		//Register air block
		BlockFactory airBlock = new BlockFactory((args) -> new BWBlock(Blocks.air) {
			@Override
			public String getID() {
				return "air";
			}
		});

		blockManager.register(airBlock);

		blockManager.registry.forEach(this::addNOVABlock);
		blockManager.whenBlockRegistered(this::onBlockRegistered);
	}

	public net.minecraft.block.Block getMCBlock(BlockFactory blockFactory) {
		return blockFactoryMap.get(blockFactory);
	}

	public net.minecraft.block.Block getMCBlock(Block novaBlock) {
		return getMCBlock(novaBlock.factory());
	}

	private void onBlockRegistered(BlockManager.BlockRegisteredEvent event) {
		addNOVABlock(event.blockFactory);
	}

	private void addNOVABlock(BlockFactory blockFactory) {
		FWBlock blockWrapper = new FWBlock(blockFactory);
		blockFactoryMap.put(blockFactory, blockWrapper);
		NovaMinecraft.proxy.registerBlock(blockWrapper);
		GameRegistry.registerBlock(blockWrapper, FWItemBlock.class, blockFactory.getID());

		if (blockWrapper.block instanceof Category && FMLCommonHandler.instance().getSide().isClient()) {
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
