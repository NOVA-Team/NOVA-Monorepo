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

package nova.core.wrapper.mc.forge.v1_8.asm.lib;

import com.google.common.base.Objects;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import nova.internal.core.Game;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import java.io.IOException;

/**
 * @author ChickenBones
 */
public class ObfMapping {
	public static final boolean obfuscated;
	/**
	 * CCC will deal withPriority this.
	 */
	public static Remapper runtimeMapper = FMLDeobfuscatingRemapper.INSTANCE;
	public static Remapper mcpMapper = null;

	static {
		boolean obf = true;
		try {
			obf = ((LaunchClassLoader) ObfMapping.class.getClassLoader()).getClassBytes("net.minecraft.world.World") == null;
		} catch (IOException iox) {
			Game.logger().error(iox.getMessage());
		}
		obfuscated = obf;
	}

	public String s_owner;
	public String s_name;
	public String s_desc;

	public boolean runtime;

	public ObfMapping(String owner) {
		this(owner, "", "");
	}

	public ObfMapping(String owner, String name, String desc) {
		this.s_owner = owner;
		this.s_name = name;
		this.s_desc = desc;

		if (s_owner.contains(".")) {
			throw new IllegalArgumentException(s_owner);
		}

		if (mcpMapper != null) {
			map(mcpMapper);
		}
	}

	public ObfMapping(ObfMapping descmap, String subclass) {
		this(subclass, descmap.s_name, descmap.s_desc);
	}

	public static ObfMapping fromDesc(String s) {
		int lastDot = s.lastIndexOf('.');
		if (lastDot < 0) {
			return new ObfMapping(s, "", "");
		}
		int sep = s.indexOf('(');// methods
		int sep_end = sep;
		if (sep < 0) {
			sep = s.indexOf(' ');// some stuffs
			sep_end = sep + 1;
		}
		if (sep < 0) {
			sep = s.indexOf(':');// fields
			sep_end = sep + 1;
		}
		if (sep < 0) {
			return new ObfMapping(s.substring(0, lastDot), s.substring(lastDot + 1), "");
		}

		return new ObfMapping(s.substring(0, lastDot), s.substring(lastDot + 1, sep), s.substring(sep_end));
	}

	public ObfMapping subclass(String subclass) {
		return new ObfMapping(this, subclass);
	}

	public boolean matches(MethodNode node) {
		return s_name.equals(node.name) && s_desc.equals(node.desc);
	}

	public boolean matches(MethodInsnNode node) {
		return s_owner.equals(node.owner) && s_name.equals(node.name) && s_desc.equals(node.desc);
	}

	@SuppressWarnings("deprecation")
	public AbstractInsnNode toInsn(int opcode) {
		if (isClass()) {
			return new TypeInsnNode(opcode, s_owner);
		} else if (isMethod()) {
			return new MethodInsnNode(opcode, s_owner, s_name, s_desc);
		} else {
			return new FieldInsnNode(opcode, s_owner, s_name, s_desc);
		}
	}

	public void visitTypeInsn(MethodVisitor mv, int opcode) {
		mv.visitTypeInsn(opcode, s_owner);
	}

	@SuppressWarnings("deprecation")
	public void visitMethodInsn(MethodVisitor mv, int opcode) {
		mv.visitMethodInsn(opcode, s_owner, s_name, s_desc);
	}

	public void visitFieldInsn(MethodVisitor mv, int opcode) {
		mv.visitFieldInsn(opcode, s_owner, s_name, s_desc);
	}

	public boolean isClass(String name) {
		return name.replace('.', '/').equals(s_owner);
	}

	public boolean matches(String name, String desc) {
		return s_name.equals(name) && s_desc.equals(desc);
	}

	public boolean matches(FieldNode node) {
		return s_name.equals(node.name) && s_desc.equals(node.desc);
	}

	public boolean matches(FieldInsnNode node) {
		return s_owner.equals(node.owner) && s_name.equals(node.name) && s_desc.equals(node.desc);
	}

	public String javaClass() {
		return s_owner.replace('/', '.');
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ObfMapping)) {
			return false;
		}

		ObfMapping desc = (ObfMapping) obj;
		return s_owner.equals(desc.s_owner) && s_name.equals(desc.s_name) && s_desc.equals(desc.s_desc);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(s_desc, s_name, s_owner);
	}

	@Override
	public String toString() {
		if (s_name.length() == 0) {
			return "[" + s_owner + "]";
		}
		if (s_desc.length() == 0) {
			return "[" + s_owner + "." + s_name + "]";
		}
		return "[" + (isMethod() ? methodDesc() : fieldDesc()) + "]";
	}

	public String methodDesc() {
		return s_owner + "." + s_name + s_desc;
	}

	public String fieldDesc() {
		return s_owner + "." + s_name + ":" + s_desc;
	}

	public boolean isClass() {
		return s_name.length() == 0;
	}

	public boolean isMethod() {
		return s_desc.contains("(");
	}

	public boolean isField() {
		return !isClass() && !isMethod();
	}

	public ObfMapping map(Remapper mapper) {
		if (isMethod()) {
			s_name = mapper.mapMethodName(s_owner, s_name, s_desc);
		} else if (isField()) {
			s_name = mapper.mapFieldName(s_owner, s_name, s_desc);
		}

		s_owner = mapper.mapType(s_owner);

		if (isMethod()) {
			s_desc = mapper.mapMethodDesc(s_desc);
		} else if (s_desc.length() > 0) {
			s_desc = mapper.mapDesc(s_desc);
		}

		return this;
	}

	public ObfMapping toRuntime() {
		if (!runtime) {
			map(runtimeMapper);
		}

		runtime = true;
		return this;
	}

	public ObfMapping copy() {
		return new ObfMapping(s_owner, s_name, s_desc);
	}
}
