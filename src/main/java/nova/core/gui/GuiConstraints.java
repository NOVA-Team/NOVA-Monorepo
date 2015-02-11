package nova.core.gui;

import nova.core.gui.layout.Constraints;
import nova.core.player.Player;
import nova.core.util.transform.Vector3i;

// TODO Make generic parameter for GUI? Could be used for container GUIs as well.
public class GuiConstraints extends Constraints<GuiConstraints> {

	public Player player;
	public Vector3i position;

	public GuiConstraints(Player player, Vector3i position) {
		this.player = player;
		this.position = position;
	}
}
