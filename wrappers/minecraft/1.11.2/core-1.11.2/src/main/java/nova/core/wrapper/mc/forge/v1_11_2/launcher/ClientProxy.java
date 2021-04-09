/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License VERSION 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either VERSION 3 of the License, or
 * (at your option) any later VERSION.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.wrapper.mc.forge.v1_11_2.launcher;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.language.LanguageManager;
import nova.core.wrapper.mc.forge.v1_11_2.NovaMinecraftPreloader;
import nova.core.wrapper.mc.forge.v1_11_2.render.RenderUtility;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward.FWTile;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward.FWTileRenderer;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.forward.FWEntity;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.forward.FWEntityRenderer;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.forward.FWItem;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.particle.backward.BWParticle;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.particle.forward.FWParticle;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * @author Calclavia
 */
public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent evt) {
		super.preInit(evt);
		MinecraftForge.EVENT_BUS.register(RenderUtility.instance);
		ClientRegistry.bindTileEntitySpecialRenderer(FWTile.class, FWTileRenderer.instance);
		RenderUtility.instance.preInit(evt);
	}

	@Override
	public void init(FMLInitializationEvent evt) {
		super.init(evt);
		RenderingRegistry.registerEntityRenderingHandler(FWEntity.class, FWEntityRenderer.instance);
	}

	@Override
	public void loadLanguage(LanguageManager languageManager) {
		super.loadLanguage(languageManager);
		ProgressManager.ProgressBar progressBar = ProgressManager.push("Loading NOVA language files",
			NovaMinecraftPreloader.novaResourcePacks.size() + 1);
		FMLProgressBar fmlProgressBar = new FMLProgressBar(progressBar);
		fmlProgressBar.step("nova");
		Minecraft.getMinecraft().getLanguageManager().getLanguages()
			.stream()
			.map(lang -> lang.getLanguageCode().replace('_', '-'))
			.forEach(langName -> {
				ResourceLocation location = new ResourceLocation("nova", langName + ".lang");
				try {
					Minecraft.getMinecraft().getResourceManager().getAllResources(location).forEach(resource ->
						loadLanguage(languageManager, location, langName, resource.getInputStream()));
				} catch (IOException ex) {
				}
			});
		NovaMinecraftPreloader.novaResourcePacks.forEach(pack -> {
			fmlProgressBar.step(pack.getID());
			pack.getLanguageFiles().stream().forEach(location -> {
				String resourcePath = location.getResourcePath();
				String langName = resourcePath.substring(5, resourcePath.length() - 5);
				try {
					Minecraft.getMinecraft().getResourceManager().getAllResources(location).forEach(resource ->
						loadLanguage(languageManager, location, langName, resource.getInputStream()));
				} catch (IOException ex) {
				}
			});
		});
		fmlProgressBar.finish();
		ProgressManager.pop(progressBar);
	}

	private void loadLanguage(LanguageManager languageManager, ResourceLocation location, String langName, InputStream stream) {
		try {
			Properties p = new Properties();
			p.load(stream);
			p.entrySet().stream().forEach(e -> languageManager.register(langName, e.getKey().toString(), e.getValue().toString()));
		} catch (IOException ex) {
		}
	}

	@Override
	public void registerItem(FWItem item) {
		super.registerItem(item);

		//Hacks to inject custom item definition
		ModelLoader.setCustomMeshDefinition(item, stack -> new ModelResourceLocation(Item.REGISTRY.getNameForObject(item), "inventory"));
	}

	@Override
	public void postRegisterBlock(FWBlock block) {
		super.postRegisterBlock(block);

		//Hack to inject custom itemblock definition
		Item itemFromBlock = Item.getItemFromBlock(block);

		ModelLoader.setCustomMeshDefinition(itemFromBlock, stack -> new ModelResourceLocation(Item.REGISTRY.getNameForObject(itemFromBlock), "inventory"));
	}

	@Override
	public boolean isPaused() {
		if (FMLClientHandler.instance().getClient().isSingleplayer() &&
			Optional.ofNullable(FMLClientHandler.instance().getClient().getIntegratedServer()).map(is -> !is.getPublic()).orElse(true)) {
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
		if (build instanceof BWParticle) {
			Particle particle = ((BWParticle) build).createParticle(world);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
			return Game.natives().toNova(particle);
		} else {
			FWParticle bwEntityFX = new FWParticle(world, factory);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(bwEntityFX);
			return bwEntityFX.wrapped;
		}
	}

	@Override
	public Entity spawnParticle(net.minecraft.world.World world, Entity entity) {
		//Backward entity particle unwrapper
		if (entity instanceof BWParticle) {
			Particle particle = ((BWParticle) entity).createParticle(world);
			Vector3D position = entity.position();
			particle.posX = position.getX();
			particle.posY = position.getY();
			particle.posZ = position.getZ();
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
			return Game.natives().toNova(particle);
		} else {
			FWParticle bwEntityFX = new FWParticle(world, entity);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(bwEntityFX);
			return bwEntityFX.wrapped;
		}
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}
}
