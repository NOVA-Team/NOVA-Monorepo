package nova.core.item;

import nova.core.entity.Entity;
import nova.core.inventory.Inventory;
import nova.core.player.Player;
import nova.core.render.texture.ItemTexture;
import nova.core.util.Direction;
import nova.core.util.Identifiable;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.core.world.World;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

/**
 * Item that can be stacked using {@link ItemStack}
 * @see ItemStack
 * @see Inventory
 */
public abstract class Item implements Identifiable {

	/**
	 * Gets a list of tooltips for this item.
	 * @return The tooltip strings
	 */
	public Collection<String> getTooltips(Optional<Player> player) {
		return new HashSet<>();
	}

	/**
	 * Called when the entity right clicks the item onto the block.
	 * @param entity - The entity using the item
	 * @param world - The world of the block
	 * @param position - The position of the block
	 * @param side - The side the player clicked
	 * @param hit - The position the player hit on the block
	 * @return True if the player's action cancels out events.
	 */
	public boolean onUse(Entity entity, World world, Vector3i position, Direction side, Vector3d hit) {
		return false;
	}

	/**
	 * Called when an entity right clicks the item.
	 * @param entity - The entity right clicking.
	 */
	public void onRightClick(Entity entity) {

	}

	/**
	 * Called when this item is being rendered.
	 * @param type Type
	 * @param data Data
	 */
	public void onRender(int type, Object... data) {

	}

	/**
	 * Gets the texture of this item. If there is no texture provided, it will not render any and default to onRender() method for custom item rendering.
	 * @return {@link ItemTexture} instance
	 */
	public Optional<ItemTexture> getTexture() {
		return Optional.empty();
	}
}
