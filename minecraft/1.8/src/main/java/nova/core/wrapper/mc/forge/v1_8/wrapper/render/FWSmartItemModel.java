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

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartItemModel;
import nova.core.component.renderer.Renderer;
import nova.core.item.Item;
import nova.core.wrapper.mc.forge.v1_8.wrapper.item.ItemConverter;

import java.util.List;

import javax.vecmath.Vector3f;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public class FWSmartItemModel extends FWSmartModel implements ISmartItemModel, IFlexibleBakedModel {

	private final Item item;

	@SuppressWarnings("deprecation")
	public FWSmartItemModel(Item item) {
		super();
		this.item = item;
		// Change the default transforms to the default Item transforms
		this.itemCameraTransforms = new net.minecraft.client.renderer.block.model.ItemCameraTransforms(
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(
				new Vector3f(0, -90, 130), new Vector3f(0, 1f / 24f, -2.75f / 16f), new Vector3f(0.9f, 0.9f, 0.9f)), // Third Person
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(
				new Vector3f(0, -135, 25/*-135/*-25*/), new Vector3f(0, 0.25f, 0.125f/*0.5f, 0.25f*/), new Vector3f(1.7f, 1.7f, 1.7f)), // First Person
			net.minecraft.client.renderer.block.model.ItemTransformVec3f.DEFAULT, // Head
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(
				new Vector3f(-30, 135, 0), new Vector3f(), new Vector3f(1.6F, 1.6F, 1.6F))); // GUI
	}

	//Item rendering
	@Override
	public ISmartItemModel handleItemState(ItemStack stack) {
		Item item = ItemConverter.instance().toNova(stack);

		if (item.components.has(Renderer.class)) {
			return new FWSmartItemModel(item);
		}

		return new FWEmptyModel();
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		BWModel model = new BWModel();
		model.matrix.translate(0.5, 0.5, 0.5);
		item.components.getSet(Renderer.class).forEach(r -> r.onRender.accept(model));
		return modelToQuads(model);
	}

	@Override
	public boolean isGui3d() {
		return item.components.has(Renderer.class);
	}
}
