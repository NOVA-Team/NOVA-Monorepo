package nova.core.wrapper.mc17.wrapper.entity.backward;

import com.google.common.collect.HashBiMap;
import net.minecraft.client.particle.Barrier;
import net.minecraft.client.particle.EntityAuraFX;
import net.minecraft.client.particle.EntityBlockDustFX;
import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.client.particle.EntityCloudFX;
import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityDropParticleFX;
import net.minecraft.client.particle.EntityEnchantmentTableParticleFX;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFireworkSparkFX;
import net.minecraft.client.particle.EntityFishWakeFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntityFootStepFX;
import net.minecraft.client.particle.EntityHeartFX;
import net.minecraft.client.particle.EntityHugeExplodeFX;
import net.minecraft.client.particle.EntityLargeExplodeFX;
import net.minecraft.client.particle.EntityLavaFX;
import net.minecraft.client.particle.EntityNoteFX;
import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.particle.EntitySnowShovelFX;
import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.client.particle.EntitySplashFX;
import net.minecraft.client.particle.EntitySuspendFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.MobAppearance;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.client.FMLClientHandler;
import nova.internal.core.Game;

import java.util.Map;

/**
 * A backward entity particle that acts as a black box, which wraps a Minecraft particle fxs.
 * @author Calclavia
 */
public class BWEntityFX extends BWEntity {

	public final EntityFX wrapped;
	private final HashBiMap<Integer, Class<? extends EntityFX>> fxMap = HashBiMap.create();

	public BWEntityFX(EntityFX wrapped) {
		super(wrapped);
		this.wrapped = wrapped;

		fxMap.put(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), EntityExplodeFX.class);
		fxMap.put(EnumParticleTypes.WATER_BUBBLE.getParticleID(), EntityBubbleFX.class);
		fxMap.put(EnumParticleTypes.WATER_SPLASH.getParticleID(), EntitySplashFX.class);
		fxMap.put(EnumParticleTypes.WATER_WAKE.getParticleID(), EntityFishWakeFX.class);
		fxMap.put(EnumParticleTypes.WATER_DROP.getParticleID(), EntityRainFX.class);
		fxMap.put(EnumParticleTypes.SUSPENDED.getParticleID(), EntitySuspendFX.class);
		fxMap.put(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), EntityAuraFX.class);
		fxMap.put(EnumParticleTypes.CRIT.getParticleID(), EntityCrit2FX.class);
		fxMap.put(EnumParticleTypes.CRIT_MAGIC.getParticleID(), EntityCrit2FX.class);
		fxMap.put(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), EntitySmokeFX.class);
		fxMap.put(EnumParticleTypes.SMOKE_LARGE.getParticleID(), EntityCritFX.class);
		fxMap.put(EnumParticleTypes.SPELL.getParticleID(), EntitySpellParticleFX.class);
		fxMap.put(EnumParticleTypes.SPELL_INSTANT.getParticleID(), EntitySpellParticleFX.class);
		fxMap.put(EnumParticleTypes.SPELL_MOB.getParticleID(), EntitySpellParticleFX.class);
		fxMap.put(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), EntitySpellParticleFX.class);
		fxMap.put(EnumParticleTypes.SPELL_WITCH.getParticleID(), EntitySpellParticleFX.class);
		fxMap.put(EnumParticleTypes.DRIP_WATER.getParticleID(), EntityDropParticleFX.class);
		fxMap.put(EnumParticleTypes.DRIP_LAVA.getParticleID(), EntityDropParticleFX.class);
		fxMap.put(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), EntityHeartFX.class);
		fxMap.put(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), EntityAuraFX.class);
		fxMap.put(EnumParticleTypes.TOWN_AURA.getParticleID(), EntityAuraFX.class);
		fxMap.put(EnumParticleTypes.NOTE.getParticleID(), EntityNoteFX.class);
		fxMap.put(EnumParticleTypes.PORTAL.getParticleID(), EntityPortalFX.class);
		fxMap.put(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), EntityEnchantmentTableParticleFX.class);
		fxMap.put(EnumParticleTypes.FLAME.getParticleID(), EntityFlameFX.class);
		fxMap.put(EnumParticleTypes.LAVA.getParticleID(), EntityLavaFX.class);
		fxMap.put(EnumParticleTypes.FOOTSTEP.getParticleID(), EntityFootStepFX.class);
		fxMap.put(EnumParticleTypes.CLOUD.getParticleID(), EntityCloudFX.class);
		fxMap.put(EnumParticleTypes.REDSTONE.getParticleID(), EntityReddustFX.class);
		fxMap.put(EnumParticleTypes.SNOWBALL.getParticleID(), EntityBreakingFX.class);
		fxMap.put(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), EntitySnowShovelFX.class);
		fxMap.put(EnumParticleTypes.SLIME.getParticleID(), EntityBreakingFX.class);
		fxMap.put(EnumParticleTypes.HEART.getParticleID(), EntityHeartFX.class);
		fxMap.put(EnumParticleTypes.ITEM_CRACK.getParticleID(), EntityBreakingFX.class);
		fxMap.put(EnumParticleTypes.BLOCK_CRACK.getParticleID(), EntityDiggingFX.class);
		fxMap.put(EnumParticleTypes.BLOCK_DUST.getParticleID(), EntityBlockDustFX.class);
		fxMap.put(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), EntityHugeExplodeFX.class);
		fxMap.put(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), EntityLargeExplodeFX.class);
		fxMap.put(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), EntityFireworkSparkFX.class);
	}

	public EntityFX createEntityFX() {
		//Look up for particle factory and pass it into BWEntityFX
		Map<Integer, IParticleFactory> particleMap = FMLClientHandler.instance().getClient().effectRenderer.field_178932_g;
		int particleID = fxMap.inverse().get(wrapped.getClass());
		IParticleFactory particleFactory = particleMap.get(particleID);
		return particleFactory.getEntityFX(0, Game.natives().toNative(world()), x(), y(), z(), 0, 0, 0, 0);
	}

	@Override
	public String getID() {
		return Game.info().name + ":" + EnumParticleTypes.getParticleFromId(fxMap.inverse().get(wrapped.getClass())).getParticleName();
	}
}
