package nova.internal.dummy;

import nova.core.item.Item;
import nova.core.player.Player;
import nova.core.recipes.crafting.CraftingGrid;

import java.util.Optional;

/**
 * @author Stan
 * @since 3/02/2015.
 */
public class CraftingGridDummy implements CraftingGrid {
	public static final CraftingGrid instance = new CraftingGridDummy();

	private CraftingGridDummy() {
	}

	@Override
	public Optional<Player> getPlayer() {
		return Optional.empty();
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Optional<Item> getStack(int slot) {
		return Optional.empty();
	}

	@Override
	public boolean setStack(int slot, Optional<Item> Item) {
		return false;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public Optional<Item> getStack(int x, int y) {
		return Optional.empty();
	}

	@Override
	public boolean setStack(int x, int y, Optional<Item> Item) {
		return false;
	}

	@Override
	public void giveBack(Item Item) {

	}

	@Override
	public String getTopology() {
		return "dummy";
	}

	@Override
	public String getType() {
		return "dummy";
	}
}
