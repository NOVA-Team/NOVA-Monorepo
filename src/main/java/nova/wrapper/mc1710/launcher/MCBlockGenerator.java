package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.registry.GameRegistry;
import nova.core.game.Game;
import nova.wrapper.mc1710.forward.block.BlockWrapper;

/**
 * Generates Minecraft blocks from NOVA blocks
 * @author Calclavia
 */
public class MCBlockGenerator {

	public void init() {
		Game.instance.get().blockManager.registry.forEach(b ->
		{
			BlockWrapper newBlock = new BlockWrapper(b);
			GameRegistry.registerBlock(newBlock, b.getID());
		});
	}

}
