package nova.core.wrapper.mc18.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import nova.core.entity.component.Player;

import java.util.Optional;

/**
 * Wrap utility methods.
 * @author Calclavia
 */
public class WrapUtility {

	public static Optional<Player> getNovaPlayer(EntityPlayer player) {
		// TODO: implement
		return Optional.empty();
	}

	public static String getItemID(Item item, int meta) {
		if (item.getHasSubtypes()) {
			return Item.itemRegistry.getNameForObject(item) + ":" + meta;
		} else {
			return (String) Item.itemRegistry.getNameForObject(item);
		}
	}

	public EntityPlayer getMCPlayer(Optional<Player> player) {
		return null;
	}
}
