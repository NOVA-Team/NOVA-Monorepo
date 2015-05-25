package nova.wrapper.mc1710.backward.entity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.DamageSource;
import nova.core.entity.component.Damageable;
import nova.core.player.InventoryPlayer;
import nova.core.player.Player;
import nova.core.util.transform.vector.Vector3d;
import nova.wrapper.mc1710.backward.inventory.BWInventory;

/**
 * A Nova to Minecraft entity wrapper
 * 
 * @author Calclavia
 */
public class BWEntityPlayer extends BWEntity implements Player, Damageable {

	public final net.minecraft.entity.player.EntityPlayer entity;
	public final BWInventoryPlayer inventory;

	public BWEntityPlayer(net.minecraft.entity.player.EntityPlayer entity) {
		// TODO: Should this be entity ID?
		super(entity);
		this.entity = entity;
		this.inventory = new BWInventoryPlayer(entity.inventory);
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

	@Override
	public void setPosition(Vector3d position) {
		super.setPosition(position);

		if (entity instanceof EntityPlayerMP) {
			((EntityPlayerMP) entity).playerNetServerHandler.setPlayerLocation(position.x, position.y, position.z, entity.rotationYaw, entity.rotationPitch);
		}
	}

	@Override
	public void damage(double amount, DamageType type) {

		if (type == DamageType.generic) {
			entity.attackEntityFrom(DamageSource.generic, (float) amount);
		}

		// TODO: Apply other damage source wrappers?
	}

	public class BWInventoryPlayer extends BWInventory implements InventoryPlayer {

		public BWInventoryPlayer(IInventory inventory) {
			super(inventory);
		}

		@Override
		public int getHeldSlot() {
			return entity.inventory.currentItem;
		}
	}
}
