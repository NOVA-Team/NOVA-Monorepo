package nova.core.wrapper.mc18.wrapper.block.forward;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import nova.core.component.Updater;

/**
 * @author Calclavia
 */
public class FWTileUpdater extends FWTile implements IUpdatePlayerListBox {
	public FWTileUpdater() {

	}

	public FWTileUpdater(String blockID) {
		this.blockID = blockID;
	}

	/**
	 * Updates the block.
	 */
	@Override
	public void update() {
		if (block != null) {
			((Updater) block).update(0.05);
		}
	}
}
