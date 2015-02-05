package nova.internal.dummy;

import nova.core.item.ItemStack;
import nova.core.player.Player;
import nova.core.recipes.crafting.CraftingGrid;

import java.util.Optional;

/**
 * Created by Stan on 3/02/2015.
 */
public class CraftingGridDummy implements CraftingGrid {
	public static final CraftingGrid INSTANCE = new CraftingGridDummy();

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
	public Optional<ItemStack> getStack(int slot) {
		return Optional.empty();
	}

	@Override
	public boolean setStack(int slot, Optional<ItemStack> itemStack) {
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
	public Optional<ItemStack> getStack(int x, int y) {
		return Optional.empty();
	}

	@Override
	public boolean setStack(int x, int y, Optional<ItemStack> itemStack) {
		return false;
	}

	@Override
	public void giveBack(ItemStack itemStack) {

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
