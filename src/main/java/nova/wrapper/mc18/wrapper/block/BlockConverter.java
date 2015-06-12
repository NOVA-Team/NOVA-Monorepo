package nova.wrapper.mc18.wrapper.block;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.component.Category;
import nova.core.loader.Loadable;
import nova.core.nativewrapper.NativeConverter;
import nova.internal.core.Game;
import nova.wrapper.mc18.launcher.NovaMinecraft;
import nova.wrapper.mc18.util.ModCreativeTab;
import nova.wrapper.mc18.wrapper.block.backward.BWBlock;
import nova.wrapper.mc18.wrapper.block.forward.FWBlock;
import nova.wrapper.mc18.wrapper.item.FWItemBlock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Calclavia
 */
public class BlockConverter implements NativeConverter<Block, net.minecraft.block.Block>, Loadable {
	/**
	 * A map of all blockFactory to MC blocks registered
	 */
	public final HashMap<BlockFactory, net.minecraft.block.Block> blockFactoryMap = new HashMap<>();

	public static BlockConverter instance() {
		return (BlockConverter) Game.natives().getNative(Block.class, net.minecraft.block.Block.class);
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
	public void preInit() {
		BlockManager blockManager = Game.blocks();

		//Register air block
		BlockFactory airBlock = new BlockFactory((args) -> new BWBlock(Blocks.air) {
			@Override
			public void onRegister() {

			}

			@Override
			public boolean canReplace() {
				return true;
			}

			@Override
			public String getID() {
				return "air";
			}
		});

		blockManager.register(airBlock);

		//NOTE: There should NEVER be blocks already registered in preInit() stage of a NativeConverter.
		blockManager.blockRegisteredListeners.add(event -> registerNovaBlock(event.blockFactory));
	}

	private void registerNovaBlock(BlockFactory blockFactory) {
		FWBlock blockWrapper = new FWBlock(blockFactory);
		blockFactoryMap.put(blockFactory, blockWrapper);
		GameRegistry.registerBlock(blockWrapper, FWItemBlock.class, blockFactory.getID());
		NovaMinecraft.proxy.postRegisterBlock(blockWrapper);

		if (blockWrapper.block.has(Category.class) && FMLCommonHandler.instance().getSide().isClient()) {
			//Add into creative tab
			Category category = blockWrapper.block.get(Category.class);
			Optional<CreativeTabs> first = Arrays.stream(CreativeTabs.creativeTabArray)
				.filter(tab -> tab.getTabLabel().equals(category.name))
				.findFirst();
			if (first.isPresent()) {
				blockWrapper.setCreativeTab(first.get());
			} else {
				ModCreativeTab tab = new ModCreativeTab(category.name, Game.natives().toNative(category.item.get()));
				blockWrapper.setCreativeTab(tab);

				if (category.item.isPresent()) {
					tab.item = Item.getItemFromBlock(blockWrapper);
				}
			}
		}

		System.out.println("[NOVA]: Registered '" + blockFactory.getID() + "' block.");
	}
}
