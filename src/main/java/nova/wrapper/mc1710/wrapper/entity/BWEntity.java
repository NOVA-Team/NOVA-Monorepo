package nova.wrapper.mc1710.wrapper.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import nova.core.component.misc.Damageable;
import nova.core.entity.Entity;
import nova.core.entity.component.Living;
import nova.core.entity.component.Player;
import nova.core.inventory.component.InventoryPlayer;
import nova.core.network.NetworkTarget;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import nova.wrapper.mc1710.backward.inventory.BWInventory;
import nova.wrapper.mc1710.wrapper.entity.forward.MCEntityTransform;

/**
 * A Minecraft to NOVA Entity wrapper
 *
 * @author Calclavia
 */
//TODO: Incomplete. Add more components!
public class BWEntity extends Entity {

	public net.minecraft.entity.Entity entity;

	public BWEntity(net.minecraft.entity.Entity entity) {
		this.entity = entity;
		add(new MCEntityTransform(entity));

		add(new Damageable() {
			@Override
			public void damage(double amount, DamageType type) {
				if (type == DamageType.generic) {
					entity.attackEntityFrom(DamageSource.generic, (float) amount);
				}
				// TODO: Apply other damage source wrappers?
			}
		});

		if (entity instanceof EntityLivingBase) {
			Living living = add(new Living());

			living.faceDisplacement = () -> {
				if (entity instanceof EntityPlayer) {
					if (NetworkTarget.Side.get().isClient()) {
						//compatibility with eye height changing mods
						return Vector3D.PLUS_J.scalarMultiply(entity.getEyeHeight() - ((EntityPlayer) entity).getDefaultEyeHeight());
					} else {
						if (entity instanceof EntityPlayerMP && entity.isSneaking()) {
							return Vector3D.PLUS_J.scalarMultiply(entity.getEyeHeight() - 0.08);
						} else {
							return Vector3D.PLUS_J.scalarMultiply(entity.getEyeHeight());
						}
					}
				}
				return Vector3D.PLUS_J.scalarMultiply(entity.getEyeHeight());
			};

			if (entity instanceof EntityPlayer) {
				add(new MCPlayer(this));
			}
		}
	}

	@Override
	public String getID() {
		return entity.getClass().getName();
	}

	public static class MCPlayer extends Player {
		public final BWEntity bwEntity;
		public final net.minecraft.entity.player.EntityPlayer entity;
		public final BWInventoryPlayer inventory;

		public MCPlayer(BWEntity bwEntity) {
			this.bwEntity = bwEntity;
			this.entity = (EntityPlayer) bwEntity.entity;
			this.inventory = new BWInventoryPlayer(entity);
		}

		@Override
		public Entity entity() {
			return bwEntity;
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
