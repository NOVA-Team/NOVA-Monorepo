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

package nova.core.wrapper.mc.forge.v1_8.asm.transformers;

import net.minecraft.launchwrapper.IClassTransformer;
import nova.core.wrapper.mc.forge.v1_8.asm.lib.ASMHelper;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Transformers implements IClassTransformer {

	private static Map<String, List<Transformer>> transformers = new HashMap<>();

	public Transformers() {
		registerTransformer(new ChunkTransformer(), "net.minecraft.world.chunk.Chunk");
		registerTransformer(new TileEntityTransformer(), "net.minecraft.tileentity.TileEntity");
		registerTransformer(new GameDataTransformer(), "net.minecraftforge.fml.common.registry.GameData");
	}

	public static void registerTransformer(Transformer transformer, String... classes) {
		Objects.requireNonNull(classes);
		for (String clazz : classes) {
			List<Transformer> list = transformers.getOrDefault(clazz, new ArrayList<>());
			list.add(transformer);
			transformers.put(clazz, list);
		}
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformers.containsKey(transformedName)) {
			ClassNode cnode = ASMHelper.createClassNode(basicClass);
			transformers.get(transformedName).forEach(t -> t.transform(cnode));
			return ASMHelper.createBytes(cnode, 0);
		}
		return basicClass;
	}
}
