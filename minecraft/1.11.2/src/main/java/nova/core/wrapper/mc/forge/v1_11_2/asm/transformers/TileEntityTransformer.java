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

package nova.core.wrapper.mc.forge.v1_11_2.asm.transformers;

import nova.core.wrapper.mc.forge.v1_11_2.asm.lib.ASMHelper;
import nova.core.wrapper.mc.forge.v1_11_2.asm.lib.InstructionComparator;
import nova.core.wrapper.mc.forge.v1_11_2.asm.lib.ObfMapping;
import nova.internal.core.Game;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TileEntityTransformer implements Transformer {

	@Override
	public void transform(ClassNode cnode) {

		Game.logger().info("Transforming TileEntity class for dynamic instance injection.");

		ObfMapping obfMap = new ObfMapping("aqk", "a", "(Laid;Ldr;)Laqk;");
		ObfMapping deobfMap = new ObfMapping("net/minecraft/tileentity/TileEntity", "create", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/tileentity/TileEntity;");

		MethodNode method = ASMHelper.findMethod(obfMap, cnode);

		if (method == null) {
			Game.logger().warn("Lookup {} failed. You are probably in a deobf environment.", obfMap);
			method = ASMHelper.findMethod(deobfMap, cnode);

			if (method == null) {
				throw new IllegalStateException("[NOVA] Lookup " + deobfMap + " failed!");
			}
		}

		Game.logger().info("Transforming method {}", method.name);

		ASMHelper.removeBlock(method.instructions, new InstructionComparator.InsnListSection(method.instructions, 30, 33));

		InsnList list = new InsnList();
		list.add(new VarInsnNode(ALOAD, 0)); // World
		list.add(new VarInsnNode(ALOAD, 1)); // NBTTagCompound
		list.add(new VarInsnNode(ALOAD, 4)); // Class<? extends TileEntity>
		list.add(new MethodInsnNode(INVOKESTATIC, "nova/core/wrapper/mc/forge/v1_11_2/asm/StaticForwarder", "loadTileEntityHook", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/Class;)Lnet/minecraft/tileentity/TileEntity;", false));
		list.add(new VarInsnNode(ASTORE, 2)); // TileEntity

		method.instructions.insert(method.instructions.get(29), list);

		Game.logger().info("Injected instruction to method: {}", method.name);
	}
}
