package nova.wrapper.mc1710.asm;

import cpw.mods.fml.common.asm.transformers.TerminalTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

/**
* Created by asie on 1/30/15.
*/
public class NovaMinecraftTransformer implements IClassTransformer {
	public NovaMinecraftTransformer() {

	}

	@Override
	public byte[] transform(String name, String tName, byte[] bytes) {
		if (!(name.startsWith("cpw.mods.fml") && name.indexOf("asm") > 0)) {
			return bytes; // optimization
		}
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		boolean patched = false;

		for (MethodNode m: classNode.methods) {
			for (int i = 0; i < m.instructions.size(); i++) {
				AbstractInsnNode in = m.instructions.get(i);
				if (in instanceof LdcInsnNode) {
					LdcInsnNode lin = (LdcInsnNode) in;
					if (lin.cst instanceof Integer && ((Integer) lin.cst).intValue() == Opcodes.ASM4) {
						lin.cst = new Integer(Opcodes.ASM5);
						//m.instructions.set(lin, lin);
						patched = true;
					}
				}
			}
		}

		if (!patched) {
			return bytes; // optimization^2
		} else {
			System.out.println("Patched " + name);
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
}
