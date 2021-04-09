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

package nova.core.wrapper.mc.forge.v1_7_10.asm.lib;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.launchwrapper.LaunchClassLoader;
import nova.internal.core.Game;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Opcodes.RET;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * @author ChickenBones
 */
public class ASMHelper {
	public static LaunchClassLoader cl = (LaunchClassLoader) ASMHelper.class.getClassLoader();
	private static Method defineClass1;
	private static Method defineClass2;

	static {
		try {
			defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
			defineClass2 = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static MethodNode findMethod(ObfMapping methodmap, ClassNode cnode) {
		for (MethodNode mnode : cnode.methods) {
			if (methodmap.matches(mnode)) {
				return mnode;
			}
		}
		return null;
	}

	public static FieldNode findField(ObfMapping fieldmap, ClassNode cnode) {
		for (FieldNode fnode : cnode.fields) {
			if (fieldmap.matches(fnode)) {
				return fnode;
			}
		}
		return null;
	}

	public static ClassNode createClassNode(byte[] bytes) {
		return createClassNode(bytes, 0);
	}

	public static ClassNode createClassNode(byte[] bytes, int flags) {
		ClassNode cnode = new ClassNode();
		ClassReader reader = new ClassReader(bytes);
		reader.accept(cnode, flags);
		return cnode;
	}

	public static byte[] createBytes(ClassNode cnode, int flags) {
		ClassWriter cw = new CC_ClassWriter(flags);
		cnode.accept(cw);
		return cw.toByteArray();
	}

	public static byte[] writeMethods(String name, byte[] bytes, Multimap<String, MethodWriter> writers) {
		if (writers.containsKey(name)) {
			ClassNode cnode = createClassNode(bytes);

			for (MethodWriter mw : writers.get(name)) {
				MethodNode mv = findMethod(mw.method, cnode);
				if (mv == null) {
					mv = (MethodNode) cnode.visitMethod(mw.access, mw.method.s_name, mw.method.s_desc, null, mw.exceptions);
				}

				mv.access = mw.access;
				mv.instructions.clear();
				mw.write(mv);
			}

			bytes = createBytes(cnode, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		}
		return bytes;
	}

	public static byte[] injectMethods(String name, byte[] bytes, Multimap<String, MethodInjector> injectors) {
		if (injectors.containsKey(name)) {
			ClassNode cnode = createClassNode(bytes);

			for (MethodInjector injector : injectors.get(name)) {
				MethodNode method = findMethod(injector.method, cnode);
				if (method == null) {
					throw new RuntimeException("Method not found: " + injector.method);
				}
				Game.logger().info("Injecting into {}\n{}", injector.method, printInsnList(injector.injection));

				List<AbstractInsnNode> callNodes;
				if (injector.before) {
					callNodes = InstructionComparator.insnListFindStart(method.instructions, injector.needle);
				} else {
					callNodes = InstructionComparator.insnListFindEnd(method.instructions, injector.needle);
				}

				if (callNodes.size() == 0) {
					throw new RuntimeException("Needle not found in Haystack: " + injector.method + "\n" + printInsnList(injector.needle));
				}

				for (AbstractInsnNode node : callNodes) {
					if (injector.before) {
						Game.logger().info("Injected before: {}", printInsn(node));
						method.instructions.insertBefore(node, cloneInsnList(injector.injection));
					} else {
						Game.logger().info("Injected after: {}", printInsn(node));
						method.instructions.insert(node, cloneInsnList(injector.injection));
					}
				}
			}

			bytes = createBytes(cnode, ClassWriter.COMPUTE_FRAMES);
		}
		return bytes;
	}

	public static String printInsnList(InsnList list) {
		InsnListPrinter p = new InsnListPrinter();
		p.visitInsnList(list);
		return p.textString();
	}

	public static String printInsn(AbstractInsnNode insn) {
		InsnListPrinter p = new InsnListPrinter();
		p.visitInsn(insn);
		return p.textString();
	}

	public static Map<LabelNode, LabelNode> cloneLabels(InsnList insns) {
		HashMap<LabelNode, LabelNode> labelMap = new HashMap<LabelNode, LabelNode>();
		for (AbstractInsnNode insn = insns.getFirst(); insn != null; insn = insn.getNext()) {
			if (insn.getType() == 8) {
				labelMap.put((LabelNode) insn, new LabelNode());
			}
		}
		return labelMap;
	}

	public static InsnList cloneInsnList(InsnList insns) {
		return cloneInsnList(cloneLabels(insns), insns);
	}

	public static InsnList cloneInsnList(Map<LabelNode, LabelNode> labelMap, InsnList insns) {
		InsnList clone = new InsnList();
		for (AbstractInsnNode insn = insns.getFirst(); insn != null; insn = insn.getNext()) {
			clone.add(insn.clone(labelMap));
		}

		return clone;
	}

	public static List<TryCatchBlockNode> cloneTryCatchBlocks(Map<LabelNode, LabelNode> labelMap, List<TryCatchBlockNode> tcblocks) {
		ArrayList<TryCatchBlockNode> clone = new ArrayList<TryCatchBlockNode>();
		for (TryCatchBlockNode node : tcblocks) {
			clone.add(new TryCatchBlockNode(labelMap.get(node.start), labelMap.get(node.end), labelMap.get(node.handler), node.type));
		}

		return clone;
	}

	public static List<LocalVariableNode> cloneLocals(Map<LabelNode, LabelNode> labelMap, List<LocalVariableNode> locals) {
		ArrayList<LocalVariableNode> clone = new ArrayList<LocalVariableNode>();
		for (LocalVariableNode node : locals) {
			clone.add(new LocalVariableNode(node.name, node.desc, node.signature, labelMap.get(node.start), labelMap.get(node.end), node.index));
		}

		return clone;
	}

	public static void copy(MethodNode src, MethodNode dst) {
		Map<LabelNode, LabelNode> labelMap = cloneLabels(src.instructions);
		dst.instructions = cloneInsnList(labelMap, src.instructions);
		dst.tryCatchBlocks = cloneTryCatchBlocks(labelMap, src.tryCatchBlocks);
		if (src.localVariables != null) {
			dst.localVariables = cloneLocals(labelMap, src.localVariables);
		}
		dst.visibleAnnotations = src.visibleAnnotations;
		dst.invisibleAnnotations = src.invisibleAnnotations;
		dst.visitMaxs(src.maxStack, src.maxLocals);
	}

	public static byte[] alterMethods(String name, byte[] bytes, HashMultimap<String, MethodAlterator> altercators) {
		if (altercators.containsKey(name)) {
			ClassNode cnode = createClassNode(bytes);

			for (MethodAlterator injector : altercators.get(name)) {
				MethodNode method = findMethod(injector.method, cnode);
				if (method == null) {
					throw new RuntimeException("Method not found: " + injector.method);
				}

				injector.alter(method);
			}

			bytes = createBytes(cnode, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		}
		return bytes;
	}

	public static int getLocal(List<LocalVariableNode> list, String name) {
		int found = -1;
		for (LocalVariableNode node : list) {
			if (node.name.equals(name)) {
				if (found >= 0) {
					throw new RuntimeException("Duplicate local variable: " + name + " not coded to handle this scenario.");
				}

				found = node.index;
			}
		}
		return found;
	}

	public static void replaceMethodCode(MethodNode original, MethodNode replacement) {
		original.instructions.clear();
		if (original.localVariables != null) {
			original.localVariables.clear();
		}
		if (original.tryCatchBlocks != null) {
			original.tryCatchBlocks.clear();
		}
		replacement.accept(original);
	}

	public static String printInsnList(InstructionComparator.InsnListSection subsection) {
		InsnListPrinter p = new InsnListPrinter();
		p.visitInsnList(subsection);
		return p.textString();
	}

	public static void removeBlock(InsnList insns, InstructionComparator.InsnListSection block) {
		AbstractInsnNode insn = block.first;
		while (true) {
			AbstractInsnNode next = insn.getNext();
			insns.remove(insn);
			if (insn == block.last) {
				break;
			}
			insn = next;
		}
	}

	public static boolean isReturn(AbstractInsnNode node) {
		switch (node.getOpcode()) {
			case RET:
			case RETURN:
			case ARETURN:
			case DRETURN:
			case FRETURN:
			case IRETURN:
			case LRETURN:
				return true;

			default:
				return false;
		}
	}

	public static String[] getExceptionTypes(Executable exec) {
		return Arrays.stream(exec.getExceptionTypes()).map(Type::getInternalName).toArray(s -> new String[s]);
	}

	public static <T> Class<T> defineClass(ClassNode cn, int flags) {
		try {
			byte[] bytes = createBytes(cn, flags);
			defineClass1.setAccessible(true);
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) defineClass1.invoke(Thread.currentThread().getContextClassLoader(), cn.name.replaceAll("/", "."), bytes, 0, bytes.length);
			defineClass1.setAccessible(false);
			return clazz;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> Class<T> defineClass(ClassNode cn, int flags, ProtectionDomain domain) {
		if (domain == null) {
			return defineClass(cn, flags);
		}
		try {
			byte[] bytes = createBytes(cn, flags);
			defineClass2.setAccessible(true);
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) defineClass2.invoke(Thread.currentThread().getContextClassLoader(), cn.name.replaceAll("/", "."), bytes, 0, bytes.length, domain);
			defineClass2.setAccessible(false);
			return clazz;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static class CodeBlock {
		public Label start = new Label();
		public Label end = new Label();
	}

	public static class ForBlock extends CodeBlock {
		public Label cmp = new Label();
		public Label inc = new Label();
		public Label body = new Label();
	}

	public static abstract class MethodAlterator {
		public final ObfMapping method;

		public MethodAlterator(ObfMapping method) {
			this.method = method;
		}

		public abstract void alter(MethodNode mv);
	}

	public static abstract class MethodWriter {
		public final int access;
		public final ObfMapping method;
		public final String[] exceptions;

		public MethodWriter(int access, ObfMapping method) {
			this(access, method, null);
		}

		public MethodWriter(int access, ObfMapping method, String[] exceptions) {
			this.access = access;
			this.method = method;
			this.exceptions = exceptions;
		}

		public abstract void write(MethodNode mv);
	}

	public static class MethodInjector {
		public final ObfMapping method;
		public final InsnList needle;
		public final InsnList injection;
		public final boolean before;

		public MethodInjector(ObfMapping method, InsnList needle, InsnList injection, boolean before) {
			this.method = method;
			this.needle = needle;
			this.injection = injection;
			this.before = before;
		}
	}
}
