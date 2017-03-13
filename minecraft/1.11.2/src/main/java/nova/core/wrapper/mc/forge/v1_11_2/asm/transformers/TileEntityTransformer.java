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
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class TileEntityTransformer implements Transformer {

	/**
	 * If the TileEntity byte code should be written to a debug file. (One for the unmodified version, and one for the modified version)
	 */
	public static boolean writeDebug = false;

	@Override
	public void transform(ClassNode cnode) {

		System.out.println("[NOVA] Transforming TileEntity class for dynamic instance injection.");

		ObfMapping obfMap = new ObfMapping("aqk", "a", "(Laid;Ldr;)Laqk;");
		ObfMapping deobfMap = new ObfMapping("net/minecraft/tileentity/TileEntity", "create", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/tileentity/TileEntity;");

		MethodNode method = ASMHelper.findMethod(obfMap, cnode);

		boolean deobf = false;

		if (method == null) {
			System.out.println("[NOVA] Lookup " + obfMap + " failed. You are probably in a deobf environment.");
			method = ASMHelper.findMethod(deobfMap, cnode);
			deobf = true;

			if (method == null) {
				System.out.println("[NOVA] Lookup " + deobfMap + " failed!");
				return;
			}
		}

		System.out.println("[NOVA] Transforming method " + method.name);

		if (writeDebug) {
			try {
				File root = new File("NOVA-Debug");
				if (!root.exists())
					root.mkdir();

				File file = new File(root, "TileEntityOriginal.java");
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				PrintWriter printWriter = new PrintWriter(new FileOutputStream(file));
				TraceClassVisitor traceClassVisitor = new TraceClassVisitor(printWriter);
				cnode.accept(traceClassVisitor);
			} catch (IOException ex) {}
		}

		ASMHelper.removeBlock(method.instructions, new InstructionComparator.InsnListSection(method.instructions, 30, 33));

		InsnList list = new InsnList();
		list.add(new VarInsnNode(ALOAD, 0)); // World
		list.add(new VarInsnNode(ALOAD, 1)); // NBTTagCompound
		list.add(new VarInsnNode(ALOAD, 4)); // Class<? extends TileEntity>
		list.add(new MethodInsnNode(INVOKESTATIC, "nova/core/wrapper/mc/forge/v1_11_2/asm/StaticForwarder", "loadTileEntityHook", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/Class;)Lnet/minecraft/tileentity/TileEntity;", false));
		list.add(new VarInsnNode(ASTORE, 2)); // TileEntity

		method.instructions.insert(method.instructions.get(29), list);

		if (writeDebug) {
			try {
				File file = new File("NOVA-Debug/TileEntityModified.java");
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				PrintWriter printWriter = new PrintWriter(new FileOutputStream(file));
				TraceClassVisitor traceClassVisitor = new TraceClassVisitor(printWriter);
				cnode.accept(traceClassVisitor);
			} catch (IOException ex) {}
		}

		System.out.println("[NOVA] Injected instruction to method: " + method.name);
	}
}
