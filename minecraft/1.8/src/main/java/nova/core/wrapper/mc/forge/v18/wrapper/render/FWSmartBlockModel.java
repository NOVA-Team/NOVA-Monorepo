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

package nova.core.wrapper.mc.forge.v18.wrapper.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import nova.core.block.Block;
import nova.core.component.renderer.DynamicRenderer;
import nova.core.component.renderer.Renderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.item.ItemBlock;
import nova.core.wrapper.mc.forge.v18.wrapper.block.forward.FWBlock;
import nova.internal.core.Game;

import java.util.List;
import javax.vecmath.Vector3f;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public class FWSmartBlockModel extends FWSmartModel implements ISmartBlockModel, ISmartItemModel, IFlexibleBakedModel {

	private final Block block;
	private final boolean isItem;

	@SuppressWarnings("deprecation")
	public FWSmartBlockModel(Block block, boolean isItem) {
		super();
		this.block = block;
		this.isItem = isItem;
		// Change the default transforms to the default full Block transforms
		this.itemCameraTransforms = new ItemCameraTransforms(
			new ItemTransformVec3f(new Vector3f(10, -45, 170), new Vector3f(0, 0.09375f, -0.171875f), new Vector3f(0.375f, 0.375f, 0.375f)), // Third Person
			ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
	}

	//Block rendering
	@Override
	public ISmartBlockModel handleBlockState(IBlockState state) {
		FWBlock block = (FWBlock) state.getBlock();

		Block blockInstance = block.getBlockInstance(block.lastExtendedWorld, Game.natives().toNova(block.lastExtendedStatePos));

		if (blockInstance.components.has(StaticRenderer.class)) {
			return new FWSmartBlockModel(blockInstance, false);
		}

		return new FWEmptyModel();
	}

	//Item rendering
	@Override
	public ISmartItemModel handleItemState(ItemStack stack) {
		ItemBlock item = Game.natives().toNova(stack);

		if (item.components.has(Renderer.class) || block.components.has(Renderer.class)) {
			return new FWSmartBlockModel(block, true);
		}

		return new FWEmptyModel();
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		BWModel model = new BWModel();
		model.matrix.translate(0.5, 0.5, 0.5);

		if (isItem) {
			if (block.components.has(StaticRenderer.class)) {
				StaticRenderer staticRenderer = block.components.get(StaticRenderer.class);
				staticRenderer.onRender.accept(model);
			} else if (block.components.has(DynamicRenderer.class)) {
				DynamicRenderer dynamicRenderer = block.components.get(DynamicRenderer.class);
				dynamicRenderer.onRender.accept(model);
			}
		} else {
			StaticRenderer staticRenderer = block.components.get(StaticRenderer.class);
			staticRenderer.onRender.accept(model);
		}

		return modelToQuads(model);
	}
}
