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

package nova.core.wrapper.mc.forge.v1_7_10.asm.transformers;

import nova.core.wrapper.mc.forge.v1_7_10.asm.lib.ASMHelper;
import nova.core.wrapper.mc.forge.v1_7_10.asm.lib.ObfMapping;
import nova.internal.core.Game;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ChunkTransformer implements Transformer {

	@Override
	public void transform(ClassNode cnode) {
		Game.logger().info("Transforming Chunk class for chunkModified event.");

		ObfMapping obfMap = new ObfMapping("apx", "a", "(IIILaji;I)Z");
		ObfMapping deobfMap = new ObfMapping("net/minecraft/world/chunk/Chunk", "func_150807_a", "(IIILnet/minecraft/block/Block;I)Z");

		MethodNode method = ASMHelper.findMethod(obfMap, cnode);

		if (method == null) {
			Game.logger().warn("Lookup {} failed. You are probably in a deobf environment.", obfMap);
			method = ASMHelper.findMethod(deobfMap, cnode);

			if (method == null) {
				throw new IllegalStateException("[NOVA] Lookup " + deobfMap + " failed!");
			}
		}

		Game.logger().info("Found method {}", method.name);

		InsnList list = new InsnList();
		list.add(new VarInsnNode(ALOAD, 0));
		list.add(new VarInsnNode(ILOAD, 1));
		list.add(new VarInsnNode(ILOAD, 2));
		list.add(new VarInsnNode(ILOAD, 3));
		list.add(new VarInsnNode(ALOAD, 8));
		list.add(new VarInsnNode(ILOAD, 9));
		list.add(new VarInsnNode(ALOAD, 4));
		list.add(new VarInsnNode(ILOAD, 5));
		list.add(new MethodInsnNode(INVOKESTATIC, "nova/core/wrapper/mc/forge/v17/asm/StaticForwarder", "chunkSetBlockEvent", "(Lnet/minecraft/world/chunk/Chunk;IIILnet/minecraft/block/Block;ILnet/minecraft/block/Block;I)V", false));

		AbstractInsnNode lastInsn = method.instructions.getLast();
		while (lastInsn instanceof LabelNode || lastInsn instanceof LineNumberNode) {
			lastInsn = lastInsn.getPrevious();
		}

		if (ASMHelper.isReturn(lastInsn)) {
			method.instructions.insertBefore(lastInsn, list);
		} else {
			method.instructions.insert(list);
		}

		Game.logger().info("Injected instruction to method: {}", method.name);
	}
}
