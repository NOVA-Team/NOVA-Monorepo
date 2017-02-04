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

package nova.core.wrapper.mc.forge.v17.launcher;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.wrapper.mc.forge.v17.render.RenderUtility;
import nova.core.wrapper.mc.forge.v17.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v17.wrapper.block.forward.FWTile;
import nova.core.wrapper.mc.forge.v17.wrapper.block.forward.FWTileRenderer;
import nova.core.wrapper.mc.forge.v17.wrapper.entity.backward.BWEntityFX;
import nova.core.wrapper.mc.forge.v17.wrapper.entity.forward.FWEntity;
import nova.core.wrapper.mc.forge.v17.wrapper.entity.forward.FWEntityFX;
import nova.core.wrapper.mc.forge.v17.wrapper.entity.forward.FWEntityRenderer;
import nova.core.wrapper.mc.forge.v17.wrapper.item.FWItem;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Calclavia
 */
public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent evt) {
		super.preInit(evt);
		MinecraftForge.EVENT_BUS.register(RenderUtility.instance);
		ClientRegistry.bindTileEntitySpecialRenderer(FWTile.class, FWTileRenderer.instance);
		RenderingRegistry.registerEntityRenderingHandler(FWEntity.class, FWEntityRenderer.instance);
		RenderUtility.instance.preInit();
	}

	@Override
	public void registerItem(FWItem item) {
		super.registerItem(item);
		MinecraftForgeClient.registerItemRenderer(item, item);
	}

	@Override
	public void registerBlock(FWBlock block) {
		super.registerBlock(block);

		/**
		 * Registers a block rendering handler for this block
		 */
		RenderingRegistry.registerBlockHandler(block);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), block);
	}

	@Override
	public boolean isPaused() {
		if (FMLClientHandler.instance().getClient().isSingleplayer() && !FMLClientHandler.instance().getClient().getIntegratedServer().getPublic()) {
			GuiScreen screen = FMLClientHandler.instance().getClient().currentScreen;
			if (screen != null) {
				if (screen.doesGuiPauseGame()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Entity spawnParticle(net.minecraft.world.World world, EntityFactory factory) {
		//Backward entity particle unwrapper
		Entity build = factory.build();
		if (build instanceof BWEntityFX) {
			EntityFX entityFX = ((BWEntityFX) build).createEntityFX();
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(entityFX);
			return Game.natives().toNova(entityFX);
		} else {
			FWEntityFX bwEntityFX = new FWEntityFX(world, factory);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(bwEntityFX);
			return bwEntityFX.wrapped;
		}
	}

	@Override
	public Entity spawnParticle(net.minecraft.world.World world, Entity entity) {
		//Backward entity particle unwrapper
		if (entity instanceof BWEntityFX) {
			EntityFX entityFX = ((BWEntityFX) entity).createEntityFX();
			Vector3D position = entity.position();
			entityFX.posX = position.getX();
			entityFX.posY = position.getY();
			entityFX.posZ = position.getZ();
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(entityFX);
			return Game.natives().toNova(entityFX);
		} else {
			FWEntityFX bwEntityFX = new FWEntityFX(world, entity);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(bwEntityFX);
			return bwEntityFX.wrapped;
		}
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
