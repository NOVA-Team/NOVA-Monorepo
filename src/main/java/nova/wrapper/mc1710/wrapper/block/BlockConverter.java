package nova.wrapper.mc1710.wrapper.block;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.game.Game;
import nova.core.nativewrapper.NativeConverter;
import nova.core.util.Category;
import nova.wrapper.mc1710.wrapper.block.backward.BWBlock;
import nova.wrapper.mc1710.launcher.NovaMinecraft;
import nova.wrapper.mc1710.util.ModCreativeTab;
import nova.wrapper.mc1710.wrapper.block.forward.FWBlock;
import nova.wrapper.mc1710.wrapper.item.FWItemBlock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Calclavia
 */
public class BlockConverter implements NativeConverter<Block, net.minecraft.block.Block> {
	/**
	 * A map of all blockFactory to MC blocks registered
	 */
	public final HashMap<BlockFactory, net.minecraft.block.Block> blockFactoryMap = new HashMap<>();

	public static BlockConverter instance() {
		return (BlockConverter) Game.instance.nativeManager.getNative(Block.class, net.minecraft.block.Block.class);
	}

	@Override
	public Class<Block> getNovaSide() {
		return Block.class;
	}

	@Override
	public Class<net.minecraft.block.Block> getNativeSide() {
		return net.minecraft.block.Block.class;
	}

	@Override
	public Block toNova(net.minecraft.block.Block nativeBlock) {
		//Prevent recursive wrapping
		if (nativeBlock instanceof FWBlock) {
			return ((FWBlock) nativeBlock).block;
		}

		return new BWBlock(nativeBlock);
	}

	@Override
	public net.minecraft.block.Block toNative(Block novaBlock) {
		//Prevent recursive wrapping
		if (novaBlock instanceof BWBlock) {
			return ((BWBlock) novaBlock).mcBlock;
		}

		return toNative(novaBlock.factory());
	}

	public net.minecraft.block.Block toNative(BlockFactory blockFactory) {
		return blockFactoryMap.get(blockFactory);
	}

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

		//Register all blocks that are already registered.
		blockManager.registry.forEach(this::addNOVABlock);
		//Continue registering blocks afterwords.
		blockManager.whenBlockRegistered(this::onBlockRegistered);
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
