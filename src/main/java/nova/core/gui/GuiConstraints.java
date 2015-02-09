package nova.core.gui;

import nova.core.gui.layout.Constraints;
import nova.core.player.Player;

// TODO Make generic parameter for GUI? Could be used for container GUIs as well.
public class GuiConstraints extends Constraints<GuiConstraints> {

	public Player player;

	public GuiConstraints(Player player) {
		this.player = player;
	}
}
