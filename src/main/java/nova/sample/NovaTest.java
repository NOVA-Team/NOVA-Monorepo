package nova.sample;

import nova.core.block.Block;
import nova.core.block.BlockBuilder;
import nova.core.game.Game;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;

/**
 * A test Nova Mod
 * @author Calclavia
 */
@NovaMod(id = "Nova Test", name = "Nova Test", version = "0.0.1", novaVersion = "0.0.1")
public class NovaTest implements Loadable {

	public Block blockTest;

	@Override
	public void preInit() {

		try {
			BlockBuilder<BlockTest> bb = new BlockBuilder<BlockTest>(BlockTest.class);
			blockTest = Game.instance.get().blockManager.registerBlock(bb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
