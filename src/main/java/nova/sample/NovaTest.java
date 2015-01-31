package nova.sample;

import nova.core.block.Block;
import nova.core.game.Game;
import nova.core.item.Item;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;
import nova.core.render.BlockTexture;

/**
 * A test Nova Mod
 * @author Calclavia
 */
@NovaMod(id = "novatest", name = "Nova Test", version = "0.0.1", novaVersion = "0.0.1")
public class NovaTest implements Loadable {

	public static Block blockTest;
	public static Item itemTest;

	public static BlockTexture steelTexture;

	@Override
	public void preInit() {
		blockTest = Game.instance.get().blockManager.registerBlock(BlockTest.class);
		itemTest = Game.instance.get().itemManager.registerItem(ItemScrewdriver.class);
		steelTexture = Game.instance.get().renderManager.registerTexture(new BlockTexture("nova:blockSteel"));
	}
}
