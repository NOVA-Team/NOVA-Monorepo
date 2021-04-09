/*
 * Copyright (c) 2017 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.wrapper.mc.forge.v1_11_2.asm.transformers;

import nova.core.wrapper.mc.forge.v1_11_2.asm.lib.ASMHelper;
import nova.core.wrapper.mc.forge.v1_11_2.asm.lib.ObfMapping;
import nova.internal.core.Game;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ExE Boss
 */
public class IForgeRegistryEntryTransformer implements Transformer {

	@Override
	public void transform(ClassNode cnode) {
		Game.logger().info("Transforming IForgeRegistryEntry class for correct NOVA mod id mapping.");

		ObfMapping mapping = new ObfMapping("net/minecraftforge/fml/common/registry/IForgeRegistryEntry$Impl", "setRegistryName", "(Ljava/lang/String;)Lnet/minecraftforge/fml/common/registry/IForgeRegistryEntry;");
		MethodNode method = ASMHelper.findMethod(mapping, cnode);

		if (method == null) {
			throw new IllegalStateException("[NOVA] Lookup " + mapping + " failed!");
		}

		Game.logger().info("Transforming method {}", method.name);

		InsnList list = new InsnList();
		list.add(new VarInsnNode(ALOAD, 5));
		list.add(new MethodInsnNode(INVOKESTATIC, "nova/core/wrapper/mc/forge/v1_11_2/asm/StaticForwarder", "isNovaPrefix", "(Ljava/lang/String;)Z", false));
		list.add(new JumpInsnNode(IFNE, (LabelNode) method.instructions.get(120)));

		method.instructions.insert(method.instructions.get(101), list);

		Game.logger().info("Injected instruction to method: {}", method.name);
	}
}
