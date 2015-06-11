package nova.wrapper.mc18.wrapper.block.forward;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import nova.core.component.Updater;

/**
 * @author Calclavia
 */
public class FWTileUpdater extends FWTile implements IUpdatePlayerListBox {
	/**
	 * Updates the block.
	 */
	@Override
	public void update() {
		((Updater) block).update(0.05);
	}
}
