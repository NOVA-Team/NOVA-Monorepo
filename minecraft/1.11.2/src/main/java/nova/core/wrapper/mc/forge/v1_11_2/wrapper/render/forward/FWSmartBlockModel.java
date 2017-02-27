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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.forward;

import nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.backward.BWModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.util.EnumFacing;
import nova.core.block.Block;
import nova.core.component.renderer.DynamicRenderer;
import nova.core.component.renderer.StaticRenderer;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public class FWSmartBlockModel extends FWSmartModel implements IBakedModel {

	private final Block block;
	private final boolean isItem;

	public FWSmartBlockModel(Block block, boolean isDummy) {
		super();
		this.block = block;
		this.isItem = isDummy;
		// Change the default transforms to the default full Block transforms
		this.itemCameraTransforms = new ItemCameraTransforms(
				new ItemTransformVec3f(new Vector3f(75, 225, 0), // Third Person (Left)
						new Vector3f(0, 0.1875f, 0.03125f), new Vector3f(0.375f, 0.375f, 0.375f)),
				new ItemTransformVec3f(new Vector3f(75, 45, 0), // Third Person (Right)
						new Vector3f(0, 0.1875f, 0.03125f), new Vector3f(0.375f, 0.375f, 0.375f)),
				new ItemTransformVec3f(new Vector3f(0, 225, 0), // First Person (Left)
						new Vector3f(0, 0, 0), new Vector3f(0.4f, 0.4f, 0.4f)),
				new ItemTransformVec3f(new Vector3f(0, 45, 0), // First Person (Right)
						new Vector3f(0, 0, 0), new Vector3f(0.4f, 0.4f, 0.4f)),
				ItemTransformVec3f.DEFAULT, // Head
				new ItemTransformVec3f(new Vector3f(30, 225, 0), // Gui
						new Vector3f(0, 0, 0), new Vector3f(0.625f, 0.625f, 0.625f)),
				new ItemTransformVec3f(new Vector3f(0, 0, 0), // Ground
						new Vector3f(0, 0, 0), new Vector3f(0, 0, 0)),
				new ItemTransformVec3f(new Vector3f(0, 0, 0), // Fixed
						new Vector3f(0, 0, 0), new Vector3f(0, 0, 0)));
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
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
			StaticRenderer renderer = block.components.get(StaticRenderer.class);
			renderer.onRender.accept(model);
		}

		return modelToQuads(model);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}
}
