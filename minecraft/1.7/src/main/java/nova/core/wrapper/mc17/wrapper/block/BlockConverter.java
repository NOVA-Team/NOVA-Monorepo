package nova.core.wrapper.mc17.wrapper.block;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.component.Category;
import nova.core.event.BlockEvent;
import nova.core.loader.Loadable;
import nova.core.nativewrapper.NativeConverter;
import nova.core.wrapper.mc17.launcher.NovaMinecraft;
import nova.core.wrapper.mc17.util.ModCreativeTab;
import nova.core.wrapper.mc17.wrapper.block.backward.BWBlock;
import nova.core.wrapper.mc17.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc17.wrapper.item.FWItemBlock;
import nova.internal.core.Game;

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
			return ((FWBlock) nativeBlock).dummy;
		}

		if (nativeBlock == Blocks.air) {
			return Game.blocks().getAirBlock().build();
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
		registerMinecraftToNOVA();
		registerNOVAToMinecraft();
	}

	private void registerMinecraftToNOVA() {
		//TODO: Will this register ALL Forge mod blocks as well?
		BlockManager blockManager = Game.blocks();
		net.minecraft.block.Block.blockRegistry.forEach(obj -> blockManager.register(new BlockFactory(() -> new BWBlock((net.minecraft.block.Block) obj), evt -> {
		})));
	}

	private void registerNOVAToMinecraft() {
		BlockManager blockManager = Game.blocks();

		//Register air block
		BlockFactory airBlock = new BlockFactory(() -> new BWBlock(Blocks.air) {
			@Override
			public boolean canReplace() {
				return true;
			}

			@Override
			public String getID() {
				return "air";
			}
		}, evt -> {
		});

		blockManager.register(airBlock);

		//NOTE: There should NEVER be blocks already registered in preInit() stage of a NativeConverter.
		Game.events().on(BlockEvent.Register.class).bind(evt -> registerNovaBlock(evt.blockFactory));
	}

	private void registerNovaBlock(BlockFactory blockFactory) {
		FWBlock blockWrapper = new FWBlock(blockFactory);
		blockFactoryMap.put(blockFactory, blockWrapper);
		NovaMinecraft.proxy.registerBlock(blockWrapper);
		GameRegistry.registerBlock(blockWrapper, FWItemBlock.class, blockFactory.getID());

		if (blockWrapper.dummy.has(Category.class) && FMLCommonHandler.instance().getSide().isClient()) {
			//Add into creative tab
			Category category = blockWrapper.dummy.get(Category.class);
			Optional<CreativeTabs> first = Arrays.stream(CreativeTabs.creativeTabArray)
				.filter(tab -> tab.getTabLabel().equals(category.name))
				.findFirst();
			if (first.isPresent()) {
				blockWrapper.setCreativeTab(first.get());
			} else {
				Optional<nova.core.item.Item> item = category.item;
				ModCreativeTab tab = new ModCreativeTab(category.name, item.isPresent() ? Game.natives().toNative(item.get()) : Item.getItemFromBlock(blockWrapper));
				blockWrapper.setCreativeTab(tab);
			}
		}

		System.out.println("[NOVA]: Registered '" + blockFactory.getID() + "' block.");
	}
}