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

package nova.core.wrapper.mc.forge.v1_8.wrapper.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import nova.core.block.Block;
import nova.core.component.renderer.Renderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.item.Item;
import nova.core.wrapper.mc.forge.v1_8.wrapper.VectorConverter;
import nova.core.wrapper.mc.forge.v1_8.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v1_8.wrapper.item.ItemConverter;

import java.util.List;
import java.util.Optional;

import javax.vecmath.Vector3f;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public class FWSmartBlockModel extends FWSmartModel implements ISmartBlockModel, ISmartItemModel, IFlexibleBakedModel {

	private final Block block;
	private final Optional<Item> item;

	public FWSmartBlockModel(Block block) {
		this(block, Optional.empty());
	}

	public FWSmartBlockModel(Block block, Item item) {
		this(block, Optional.of(item));
	}

	@SuppressWarnings("deprecation")
	public FWSmartBlockModel(Block block, Optional<Item> item) {
		super();
		this.block = block;
		this.item = item;
		// Change the default transforms to the default full Block transforms
		this.itemCameraTransforms = new net.minecraft.client.renderer.block.model.ItemCameraTransforms(
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(
				new Vector3f(10, -45, 170), new Vector3f(0, 0.09375f, -0.171875f), new Vector3f(0.375f, 0.375f, 0.375f)), // Third Person
			net.minecraft.client.renderer.block.model.ItemTransformVec3f.DEFAULT,
			net.minecraft.client.renderer.block.model.ItemTransformVec3f.DEFAULT,
			net.minecraft.client.renderer.block.model.ItemTransformVec3f.DEFAULT);
	}

	//Block rendering
	@Override
	public ISmartBlockModel handleBlockState(IBlockState state) {
		FWBlock block = (FWBlock) state.getBlock();

		Block blockInstance = block.getBlockInstance(block.lastExtendedWorld, VectorConverter.instance().toNova(block.lastExtendedStatePos));

		if (blockInstance.components.has(StaticRenderer.class)) {
			return new FWSmartBlockModel(blockInstance);
		}

		return new FWEmptyModel();
	}

	//Item rendering
	@Override
	public ISmartItemModel handleItemState(ItemStack stack) {
		Item item = ItemConverter.instance().toNova(stack);

		if (item.components.has(Renderer.class) || block.components.has(Renderer.class)) {
			return new FWSmartBlockModel(block, item);
		}

		return new FWEmptyModel();
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		BWModel model = new BWModel();
		model.matrix.translate(0.5, 0.5, 0.5);

		if (item.isPresent()) {
			if (item.get().components.has(Renderer.class)) {
				item.get().components.getSet(Renderer.class).forEach(r -> r.onRender.accept(model));
			} else {
				block.components.getSet(Renderer.class).forEach(r -> r.onRender.accept(model));
			}
		} else {
			block.components.get(StaticRenderer.class).onRender.accept(model);
		}

		return modelToQuads(model);
	}
}
