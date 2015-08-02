package nova.core.wrapper.mc18.wrapper.inventory;

import net.minecraft.inventory.IInventory;
import nova.core.inventory.Inventory;
import nova.core.nativewrapper.NativeConverter;

/**
 * @author Calclavia
 */
public class InventoryConverter implements NativeConverter<Inventory, IInventory> {
	@Override
	public Class<Inventory> getNovaSide() {
		return Inventory.class;
	}

	@Override
	public Class<IInventory> getNativeSide() {
		return IInventory.class;
	}

	@Override
	public Inventory toNova(IInventory nativeObj) {
		if (nativeObj instanceof FWInventory) {
			return ((FWInventory) nativeObj).wrapped;
		}

		return new BWInventory(nativeObj);
	}

	@Override
	public IInventory toNative(Inventory novaObj) {

		if (novaObj instanceof BWInventory) {
			return ((BWInventory) novaObj).wrapped;
		}
		return new FWInventory(novaObj);
	}

}
