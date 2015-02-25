package nova.wrapper.mc1710.backward.entity;

import net.minecraft.util.DamageSource;
import nova.core.entity.components.Damageable;
import nova.core.player.InventoryPlayer;
import nova.core.player.Player;

/**
 * A Nova to Minecraft entity wrapper
 * @author Calclavia
 */
public class BWEntityPlayer extends BWEntity implements Player, Damageable {

	public net.minecraft.entity.player.EntityPlayer entity;

	public BWEntityPlayer(net.minecraft.entity.player.EntityPlayer entity) {
		//TODO: Should this be entity ID?
		super(entity);
		this.entity = entity;
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
		// TODO
		return null;
	}

	@Override
	public String getDisplayName() {
		return entity.getDisplayName();
	}

	@Override
	public void damage(double amount, DamageType type) {

		if (type == DamageType.generic) {
			entity.attackEntityFrom(DamageSource.generic, (float) amount);
		}

		//TODO: Apply other damage source wrappers?
	}
}
