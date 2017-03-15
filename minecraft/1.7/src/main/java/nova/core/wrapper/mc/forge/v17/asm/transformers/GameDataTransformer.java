/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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
package nova.core.wrapper.mc.forge.v17.asm.transformers;

import nova.core.wrapper.mc.forge.v17.asm.lib.ASMHelper;
import nova.core.wrapper.mc.forge.v17.asm.lib.ObfMapping;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * @author ExE Boss
 */
public class GameDataTransformer implements Transformer {

	@Override
	public void transform(ClassNode cnode) {

		System.out.println("[NOVA] Transforming GameData class for correct NOVA mod id mapping.");

		ObfMapping mapping = new ObfMapping("cpw/mods/fml/common/registry/GameData", "addPrefix", "(Ljava/lang/String;)Ljava/lang/String;");

		MethodNode method = ASMHelper.findMethod(mapping, cnode);

		if (method == null) {
			throw new RuntimeException("[NOVA] Lookup " + mapping + " failed!");
		}

		System.out.println("[NOVA] Transforming method " + method.name);

		@SuppressWarnings("unchecked")
		JumpInsnNode prev = (JumpInsnNode) method.instructions.get(49);

		InsnList list = new InsnList();
		list.add(new VarInsnNode(ALOAD, 4));
		list.add(new MethodInsnNode(INVOKESTATIC, "nova/core/wrapper/mc/forge/v17/asm/StaticForwarder", "addPrefix$isNovaPrefix", "(Ljava/lang/String;)Z", false));
		list.add(new JumpInsnNode(IFNE, prev.label));

		method.instructions.insert(prev, list);

		System.out.println("[NOVA] Injected instruction to method: " + method.name);
	}
}
