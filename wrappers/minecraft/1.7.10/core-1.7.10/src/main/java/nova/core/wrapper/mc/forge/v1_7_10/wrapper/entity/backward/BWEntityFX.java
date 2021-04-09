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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.entity.backward;

import com.google.common.collect.HashBiMap;
import cpw.mods.fml.client.FMLClientHandler;
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
import nova.core.wrapper.mc.forge.v1_7_10.util.WrapperEvent;
import nova.internal.core.Game;

/**
 * A backward entity particle that acts as a black box, which wraps a Minecraft particle fxs.
 * @author Calclavia
 */
public class BWEntityFX extends BWEntity {

	public static final HashBiMap<String, Class<? extends EntityFX>> fxMap = HashBiMap.create();

	static {
		//TODO: Handle duplicate fxs
		fxMap.put("explode", EntityExplodeFX.class);
		fxMap.put("bubble", EntityBubbleFX.class);
		fxMap.put("splash", EntitySplashFX.class);
		fxMap.put("wake", EntityFishWakeFX.class);
		fxMap.put("droplet", EntityRainFX.class);
		fxMap.put("suspended", EntitySuspendFX.class);
		//fxMap.put("depthsuspend", EntityAuraFX.class);
		fxMap.put("crit", EntityCrit2FX.class);
		//fxMap.put("magicCrit", EntityCrit2FX.class);
		fxMap.put("smoke", EntitySmokeFX.class);
		fxMap.put("largesmoke", EntityCritFX.class);
		fxMap.put("spell", EntitySpellParticleFX.class);
		/*fxMap.put("instantSpell", EntitySpellParticleFX.class);
		fxMap.put("mobSpell", EntitySpellParticleFX.class);
		fxMap.put("mobSpellAmbient", EntitySpellParticleFX.class);
		fxMap.put("witchMagic", EntitySpellParticleFX.class);*/
		fxMap.put("dripWater", EntityDropParticleFX.class);
		//fxMap.put("dripLava", EntityDropParticleFX.class);
		//fxMap.put("angryVillager", EntityHeartFX.class);
		//fxMap.put("happyVillager", EntityAuraFX.class);
		fxMap.put("townaura", EntityAuraFX.class);
		fxMap.put("note", EntityNoteFX.class);
		fxMap.put("portal", EntityPortalFX.class);
		fxMap.put("enchantmenttable", EntityEnchantmentTableParticleFX.class);
		fxMap.put("flame", EntityFlameFX.class);
		fxMap.put("lava", EntityLavaFX.class);
		fxMap.put("footstep", EntityFootStepFX.class);
		fxMap.put("cloud", EntityCloudFX.class);
		fxMap.put("reddust", EntityReddustFX.class);
		//fxMap.put("snowballpoof", EntityBreakingFX.class);
		fxMap.put("snowshovel", EntitySnowShovelFX.class);
		//fxMap.put("slime", EntityBreakingFX.class);
		fxMap.put("heart", EntityHeartFX.class);
		fxMap.put("iconcrack_", EntityBreakingFX.class);
		fxMap.put("blockcrack_", EntityDiggingFX.class);
		fxMap.put("blockdust_", EntityBlockDustFX.class);
		fxMap.put("hugeexplosion", EntityHugeExplodeFX.class);
		fxMap.put("largeexplode", EntityLargeExplodeFX.class);
		fxMap.put("fireworksSpark", EntityFireworkSparkFX.class);
	}

	public final String particleID;

	public BWEntityFX(String particleID) {
		super();
		this.particleID = particleID;
	}

	public EntityFX createEntityFX() {
		//Look up for particle factory and pass it into BWEntityFX
		//TODO: Handle velocity?
		EntityFX entity = FMLClientHandler.instance().getClient().renderGlobal.doSpawnParticle(particleID, 0, 0, 0, 0, 0, 0);
		WrapperEvent.BWEntityFXCreate event = new WrapperEvent.BWEntityFXCreate(this, entity);
		Game.events().publish(event);
		return entity;
	}
}
