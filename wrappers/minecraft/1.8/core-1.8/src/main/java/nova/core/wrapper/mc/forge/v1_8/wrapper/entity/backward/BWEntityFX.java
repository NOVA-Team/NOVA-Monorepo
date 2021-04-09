/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.wrapper.mc.forge.v1_8.wrapper.entity.backward;

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
import net.minecraft.client.particle.EntityFireworkStarterFX_Factory;
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
import nova.core.wrapper.mc.forge.v1_8.util.WrapperEvent;
import nova.internal.core.Game;

import java.util.HashMap;
import java.util.Map;

/**
 * A backward entity particle that acts as a black box, which wraps a Minecraft particle fxs.
 * @author Calclavia
 */
public class BWEntityFX extends BWEntity {

	public static final HashBiMap<Integer, Class<? extends EntityFX>> FX_CLASS_MAP = HashBiMap.create();
	public static final Map<Integer, IParticleFactory> FX_FACTORY_MAP = new HashMap<>();

	static {
		//TODO: Handle duplicate fxs using a function instead of a map
		FX_CLASS_MAP.put(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), EntityExplodeFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.WATER_BUBBLE.getParticleID(), EntityBubbleFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.WATER_SPLASH.getParticleID(), EntitySplashFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.WATER_WAKE.getParticleID(), EntityFishWakeFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.WATER_DROP.getParticleID(), EntityRainFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SUSPENDED.getParticleID(), EntitySuspendFX.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), EntityAuraFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.CRIT.getParticleID(), EntityCrit2FX.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.CRIT_MAGIC.getParticleID(), EntityCrit2FX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), EntitySmokeFX.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.SMOKE_LARGE.getParticleID(), EntityCritFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SPELL.getParticleID(), EntitySpellParticleFX.class);
		/*FX_CLASS_MAP.put(EnumParticleTypes.SPELL_INSTANT.getParticleID(), EntitySpellParticleFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SPELL_MOB.getParticleID(), EntitySpellParticleFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), EntitySpellParticleFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SPELL_WITCH.getParticleID(), EntitySpellParticleFX.class);*/
		FX_CLASS_MAP.put(EnumParticleTypes.DRIP_WATER.getParticleID(), EntityDropParticleFX.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.DRIP_LAVA.getParticleID(), EntityDropParticleFX.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), EntityHeartFX.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), EntityAuraFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.TOWN_AURA.getParticleID(), EntityAuraFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.NOTE.getParticleID(), EntityNoteFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.PORTAL.getParticleID(), EntityPortalFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), EntityEnchantmentTableParticleFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.FLAME.getParticleID(), EntityFlameFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.LAVA.getParticleID(), EntityLavaFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.FOOTSTEP.getParticleID(), EntityFootStepFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.CLOUD.getParticleID(), EntityCloudFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.REDSTONE.getParticleID(), EntityReddustFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SNOWBALL.getParticleID(), EntityBreakingFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), EntitySnowShovelFX.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.SLIME.getParticleID(), EntityBreakingFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.HEART.getParticleID(), EntityHeartFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.BARRIER.getParticleID(), Barrier.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.ITEM_CRACK.getParticleID(), EntityBreakingFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.BLOCK_CRACK.getParticleID(), EntityDiggingFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.BLOCK_DUST.getParticleID(), EntityBlockDustFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), EntityHugeExplodeFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), EntityLargeExplodeFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), EntityFireworkSparkFX.class);
		FX_CLASS_MAP.put(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), MobAppearance.class);

		FX_FACTORY_MAP.put(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new EntityExplodeFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new EntityBubbleFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.WATER_SPLASH.getParticleID(), new EntitySplashFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.WATER_WAKE.getParticleID(), new EntityFishWakeFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.WATER_DROP.getParticleID(), new EntityRainFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SUSPENDED.getParticleID(), new EntitySuspendFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new EntityAuraFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.CRIT.getParticleID(), new EntityCrit2FX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new EntityCrit2FX.MagicFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new EntitySmokeFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new EntityCritFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SPELL.getParticleID(), new EntitySpellParticleFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new EntitySpellParticleFX.InstantFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SPELL_MOB.getParticleID(), new EntitySpellParticleFX.MobFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new EntitySpellParticleFX.AmbientMobFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SPELL_WITCH.getParticleID(), new EntitySpellParticleFX.WitchFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.DRIP_WATER.getParticleID(), new EntityDropParticleFX.WaterFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.DRIP_LAVA.getParticleID(), new EntityDropParticleFX.LavaFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new EntityHeartFX.AngryVillagerFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new EntityAuraFX.HappyVillagerFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.TOWN_AURA.getParticleID(), new EntityAuraFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.NOTE.getParticleID(), new EntityNoteFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.PORTAL.getParticleID(), new EntityPortalFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new EntityEnchantmentTableParticleFX.EnchantmentTable());
		FX_FACTORY_MAP.put(EnumParticleTypes.FLAME.getParticleID(), new EntityFlameFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.LAVA.getParticleID(), new EntityLavaFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.FOOTSTEP.getParticleID(), new EntityFootStepFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.CLOUD.getParticleID(), new EntityCloudFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.REDSTONE.getParticleID(), new EntityReddustFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SNOWBALL.getParticleID(), new EntityBreakingFX.SnowballFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new EntitySnowShovelFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SLIME.getParticleID(), new EntityBreakingFX.SlimeFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.HEART.getParticleID(), new EntityHeartFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.ITEM_CRACK.getParticleID(), new EntityBreakingFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new EntityDiggingFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.BLOCK_DUST.getParticleID(), new EntityBlockDustFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new EntityHugeExplodeFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new EntityLargeExplodeFX.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new EntityFireworkStarterFX_Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new MobAppearance.Factory());
	}

	private final int particleID;

	public BWEntityFX(int particleID) {
		super();
		this.particleID = particleID;
	}

	public EntityFX createEntityFX(net.minecraft.world.World world) {
		//Look up for particle factory and pass it into BWEntityFX
		IParticleFactory particleFactory = (IParticleFactory) FMLClientHandler.instance().getClient().effectRenderer.field_178932_g.get(particleID);
		EntityFX entity = particleFactory.getEntityFX(0, world, 0, 0, 0, 0, 0, 0, 0);
		WrapperEvent.BWEntityFXCreate event = new WrapperEvent.BWEntityFXCreate(this, entity);
		Game.events().publish(event);
		return entity;
	}
}
