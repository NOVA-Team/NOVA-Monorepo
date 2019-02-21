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

package nova.core.wrapper.mc.forge.v1_7_10.launcher;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.language.LanguageManager;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.forward.FWTile;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.entity.forward.FWEntity;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.item.FWItem;

import java.util.Set;

/**
 * @author Calclavia
 */
public class CommonProxy implements ForgeLoadable {
	@Override
	public void preInit(FMLPreInitializationEvent evt) {
		GameRegistry.registerTileEntity(FWTile.class, "novaTile");
		int globalUniqueEntityId = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerGlobalEntityID(FWEntity.class, "novaEntity", globalUniqueEntityId);
		EntityRegistry.registerModEntity(FWEntity.class, "novaEntity", globalUniqueEntityId, NovaMinecraft.instance, 64, 20, true);
	}

	public void loadLanguage(LanguageManager languageManager) {}

	public void registerResourcePacks(Set<Class<?>> modClasses) {

	}

	public void registerItem(FWItem item) {

	}

	public void registerBlock(FWBlock block) {

	}

	public Entity spawnParticle(net.minecraft.world.World world, EntityFactory factory) {
		return null;
	}

	public Entity spawnParticle(net.minecraft.world.World world, Entity entity) {
		return null;
	}

	public boolean isPaused() {
		return false;
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}
}
