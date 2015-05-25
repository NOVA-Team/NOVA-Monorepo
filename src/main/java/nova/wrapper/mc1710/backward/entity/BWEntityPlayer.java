package nova.wrapper.mc1710.backward.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import nova.core.entity.component.Damageable;
import nova.core.entity.component.Player;
import nova.core.inventory.component.InventoryPlayer;
import nova.wrapper.mc1710.backward.inventory.BWInventory;

/**
 * A Nova to Minecraft entity wrapper
 * @author Calclavia
 */
public class BWEntityPlayer extends BWEntity implements Damageable {

	public final net.minecraft.entity.player.EntityPlayer entity;
	public final BWInventoryPlayer inventory;

	public BWEntityPlayer(net.minecraft.entity.player.EntityPlayer entity) {
		super(entity);
		this.entity = entity;
		this.inventory = new BWInventoryPlayer(entity);
		add(new MCPlayer(entity));
	}

	@Override
	public void damage(double amount, DamageType type) {

		if (type == DamageType.generic) {
			entity.attackEntityFrom(DamageSource.generic, (float) amount);
		}

		// TODO: Apply other damage source wrappers?
	}

	public static class MCPlayer extends Player {
		public final net.minecraft.entity.player.EntityPlayer entity;
		public final BWInventoryPlayer inventory;

		public MCPlayer(EntityPlayer entity) {
			this.entity = entity;
			this.inventory = new BWInventoryPlayer(entity);
		}

		@Override
		public String getUsername() {
			return entity.getGameProfile().getName();
		}

		@Override
		public String getID() {
			return entity.getGameProfile().getId().toString();
		}

		@Override
		public InventoryPlayer getInventory() {
			return inventory;
		}

		@Override
		public String getDisplayName() {
			return entity.getDisplayName();
		}
	}

	public static class BWInventoryPlayer extends BWInventory implements InventoryPlayer {
		public final net.minecraft.entity.player.EntityPlayer entity;

		public BWInventoryPlayer(EntityPlayer entity) {
			super(entity.inventory);
			this.entity = entity;
		}

		@Override
		public int getHeldSlot() {
			return entity.inventory.currentItem;
		}
	}
}
