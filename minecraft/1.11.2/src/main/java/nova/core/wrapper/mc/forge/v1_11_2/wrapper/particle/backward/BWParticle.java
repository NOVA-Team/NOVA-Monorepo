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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.particle.backward;

import com.google.common.collect.HashBiMap;
import net.minecraft.client.particle.Barrier;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleBlockDust;
import net.minecraft.client.particle.ParticleBreaking;
import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.client.particle.ParticleCrit;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.ParticleDragonBreath;
import net.minecraft.client.particle.ParticleDrip;
import net.minecraft.client.particle.ParticleEnchantmentTable;
import net.minecraft.client.particle.ParticleEndRod;
import net.minecraft.client.particle.ParticleExplosion;
import net.minecraft.client.particle.ParticleExplosionHuge;
import net.minecraft.client.particle.ParticleExplosionLarge;
import net.minecraft.client.particle.ParticleFallingDust;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleFootStep;
import net.minecraft.client.particle.ParticleHeart;
import net.minecraft.client.particle.ParticleLava;
import net.minecraft.client.particle.ParticleMobAppearance;
import net.minecraft.client.particle.ParticleNote;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.client.particle.ParticleRain;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.client.particle.ParticleSmokeLarge;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.particle.ParticleSnowShovel;
import net.minecraft.client.particle.ParticleSpell;
import net.minecraft.client.particle.ParticleSpit;
import net.minecraft.client.particle.ParticleSplash;
import net.minecraft.client.particle.ParticleSuspend;
import net.minecraft.client.particle.ParticleSuspendedTown;
import net.minecraft.client.particle.ParticleSweepAttack;
import net.minecraft.client.particle.ParticleTotem;
import net.minecraft.client.particle.ParticleWaterWake;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.client.FMLClientHandler;
import nova.core.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * A backward entity particle that acts as a black box, which wraps a Minecraft particle.
 *
 * TODO: Minecraft particles are no longer entities.
 *
 * @author Calclavia
 */
public class BWParticle extends Entity {

	public static final HashBiMap<Integer, Class<? extends Particle>> FX_CLASS_MAP = HashBiMap.create();
	public static final Map<Integer, IParticleFactory> FX_FACTORY_MAP = new HashMap<>();

	static {
		//TODO: Handle duplicate fxs using a function instead of a map
		FX_CLASS_MAP.put(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), ParticleExplosion.class);
		FX_CLASS_MAP.put(EnumParticleTypes.WATER_BUBBLE.getParticleID(), ParticleBubble.class);
		FX_CLASS_MAP.put(EnumParticleTypes.WATER_SPLASH.getParticleID(), ParticleSplash.class);
		FX_CLASS_MAP.put(EnumParticleTypes.WATER_WAKE.getParticleID(), ParticleWaterWake.class);
		FX_CLASS_MAP.put(EnumParticleTypes.WATER_DROP.getParticleID(), ParticleRain.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SUSPENDED.getParticleID(), ParticleSuspend.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), ParticleSuspendedTown.class);
		FX_CLASS_MAP.put(EnumParticleTypes.CRIT.getParticleID(), ParticleCrit.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.CRIT_MAGIC.getParticleID(), ParticleCrit.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), ParticleSmokeNormal.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SMOKE_LARGE.getParticleID(), ParticleSmokeLarge.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SPELL.getParticleID(), ParticleSpell.class);
		/*FX_CLASS_MAP.put(EnumParticleTypes.SPELL_INSTANT.getParticleID(), ParticleSpell.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SPELL_MOB.getParticleID(), ParticleSpell.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), ParticleSpell.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SPELL_WITCH.getParticleID(), ParticleSpell.class);*/
		FX_CLASS_MAP.put(EnumParticleTypes.DRIP_WATER.getParticleID(), ParticleDrip.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.DRIP_LAVA.getParticleID(), ParticleDrip.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), ParticleHeart.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), ParticleSuspendedTown.class);
		FX_CLASS_MAP.put(EnumParticleTypes.TOWN_AURA.getParticleID(), ParticleSuspendedTown.class);
		FX_CLASS_MAP.put(EnumParticleTypes.NOTE.getParticleID(), ParticleNote.class);
		FX_CLASS_MAP.put(EnumParticleTypes.PORTAL.getParticleID(), ParticlePortal.class);
		FX_CLASS_MAP.put(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), ParticleEnchantmentTable.class);
		FX_CLASS_MAP.put(EnumParticleTypes.FLAME.getParticleID(), ParticleFlame.class);
		FX_CLASS_MAP.put(EnumParticleTypes.LAVA.getParticleID(), ParticleLava.class);
		FX_CLASS_MAP.put(EnumParticleTypes.FOOTSTEP.getParticleID(), ParticleFootStep.class);
		FX_CLASS_MAP.put(EnumParticleTypes.CLOUD.getParticleID(), ParticleCloud.class);
		FX_CLASS_MAP.put(EnumParticleTypes.REDSTONE.getParticleID(), ParticleRedstone.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SNOWBALL.getParticleID(), ParticleBreaking.class);
		FX_CLASS_MAP.put(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), ParticleSnowShovel.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.SLIME.getParticleID(), ParticleBreaking.class);
		FX_CLASS_MAP.put(EnumParticleTypes.HEART.getParticleID(), ParticleHeart.class);
		FX_CLASS_MAP.put(EnumParticleTypes.BARRIER.getParticleID(), Barrier.class);
		//FX_CLASS_MAP.put(EnumParticleTypes.ITEM_CRACK.getParticleID(), ParticleBreaking.class);
		FX_CLASS_MAP.put(EnumParticleTypes.BLOCK_CRACK.getParticleID(), ParticleDigging.class);
		FX_CLASS_MAP.put(EnumParticleTypes.BLOCK_DUST.getParticleID(), ParticleBlockDust.class);
		FX_CLASS_MAP.put(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), ParticleExplosionHuge.class);
		FX_CLASS_MAP.put(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), ParticleExplosionLarge.class);
		FX_CLASS_MAP.put(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), ParticleFirework.Spark.class);
		FX_CLASS_MAP.put(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), ParticleMobAppearance.class);
        FX_CLASS_MAP.put(EnumParticleTypes.SPIT.getParticleID(), ParticleSpit.class);
        FX_CLASS_MAP.put(EnumParticleTypes.FALLING_DUST.getParticleID(), ParticleFallingDust.class);
        FX_CLASS_MAP.put(EnumParticleTypes.DRAGON_BREATH.getParticleID(), ParticleDragonBreath.class);
        FX_CLASS_MAP.put(EnumParticleTypes.END_ROD.getParticleID(), ParticleEndRod.class);
        /*FX_CLASS_MAP.put(EnumParticleTypes.DAMAGE_INDICATOR.getParticleID(), ParticleCrit.class);*/
        FX_CLASS_MAP.put(EnumParticleTypes.SWEEP_ATTACK.getParticleID(), ParticleSweepAttack.class);
        FX_CLASS_MAP.put(EnumParticleTypes.TOTEM.getParticleID(), ParticleTotem.class);

		FX_FACTORY_MAP.put(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new ParticleExplosion.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new ParticleBubble.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.WATER_SPLASH.getParticleID(), new ParticleSplash.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.WATER_WAKE.getParticleID(), new ParticleWaterWake.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.WATER_DROP.getParticleID(), new ParticleRain.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SUSPENDED.getParticleID(), new ParticleSuspend.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new ParticleSuspendedTown.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.CRIT.getParticleID(), new ParticleCrit.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new ParticleCrit.MagicFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new ParticleSmokeNormal.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new ParticleSmokeLarge.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SPELL.getParticleID(), new ParticleSpell.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new ParticleSpell.InstantFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SPELL_MOB.getParticleID(), new ParticleSpell.MobFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new ParticleSpell.AmbientMobFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SPELL_WITCH.getParticleID(), new ParticleSpell.WitchFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.DRIP_WATER.getParticleID(), new ParticleDrip.WaterFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.DRIP_LAVA.getParticleID(), new ParticleDrip.LavaFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new ParticleHeart.AngryVillagerFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new ParticleSuspendedTown.HappyVillagerFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.TOWN_AURA.getParticleID(), new ParticleSuspendedTown.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.NOTE.getParticleID(), new ParticleNote.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.PORTAL.getParticleID(), new ParticlePortal.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new ParticleEnchantmentTable.EnchantmentTable());
		FX_FACTORY_MAP.put(EnumParticleTypes.FLAME.getParticleID(), new ParticleFlame.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.LAVA.getParticleID(), new ParticleLava.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.FOOTSTEP.getParticleID(), new ParticleFootStep.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.CLOUD.getParticleID(), new ParticleCloud.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.REDSTONE.getParticleID(), new ParticleRedstone.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SNOWBALL.getParticleID(), new ParticleBreaking.SnowballFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new ParticleSnowShovel.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.SLIME.getParticleID(), new ParticleBreaking.SlimeFactory());
		FX_FACTORY_MAP.put(EnumParticleTypes.HEART.getParticleID(), new ParticleHeart.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.ITEM_CRACK.getParticleID(), new ParticleBreaking.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new ParticleDigging.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.BLOCK_DUST.getParticleID(), new ParticleBlockDust.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new ParticleExplosionHuge.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new ParticleExplosionLarge.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new ParticleFirework.Factory());
		FX_FACTORY_MAP.put(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new ParticleMobAppearance.Factory());
        FX_FACTORY_MAP.put(EnumParticleTypes.SPIT.getParticleID(), new ParticleSpit.Factory());
        FX_FACTORY_MAP.put(EnumParticleTypes.FALLING_DUST.getParticleID(), new ParticleFallingDust.Factory());
        FX_FACTORY_MAP.put(EnumParticleTypes.DRAGON_BREATH.getParticleID(), new ParticleDragonBreath.Factory());
        FX_FACTORY_MAP.put(EnumParticleTypes.END_ROD.getParticleID(), new ParticleEndRod.Factory());
        FX_FACTORY_MAP.put(EnumParticleTypes.DAMAGE_INDICATOR.getParticleID(), new ParticleCrit.DamageIndicatorFactory());
        FX_FACTORY_MAP.put(EnumParticleTypes.SWEEP_ATTACK.getParticleID(), new ParticleSweepAttack.Factory());
        FX_FACTORY_MAP.put(EnumParticleTypes.TOTEM.getParticleID(), new ParticleTotem.Factory());
	}

	private final int particleID;

	public BWParticle(int particleID) {
		this.particleID = particleID;
	}

	public Particle createParticle(net.minecraft.world.World world) {
		//Look up for particle factory and pass it into BWParticle
		IParticleFactory particleFactory = FMLClientHandler.instance().getClient().effectRenderer.particleTypes.get(particleID);
		Particle particle = particleFactory.createParticle(0, world, 0, 0, 0, 0, 0, 0, 0);
		return particle;
	}
}
