package nova.wrapper.mc1710.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import nova.wrapper.mc1710.asm.lib.ASMHelper;
import nova.wrapper.mc1710.asm.lib.ObfMapping;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

/**
 * The Nova transformer.
 *
 * @author Calclavia
 */
public class NovaTransformer implements IClassTransformer {
	public static LaunchClassLoader cl = (LaunchClassLoader) NovaTransformer.class.getClassLoader();

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {

		/**
		 * Transform chunk to call block change events
		 */
		if (transformedName.equals("net.minecraft.world.chunk.Chunk")) {
			System.out.println("[NOVA] Transforming Chunk class for chunkModified event.");

			ClassNode cnode = ASMHelper.createClassNode(bytes);

			for (MethodNode method : cnode.methods) {
				ObfMapping m = new ObfMapping(cnode.name, method.name, method.desc).toRuntime();

				//TODO: Use obfuscation name reference
				if (m.s_name.equals("func_150807_a")) {
					System.out.println("[NOVA] Found method " + m.s_name);
					InsnList list = new InsnList();
					list.add(new VarInsnNode(ALOAD, 0));
					list.add(new VarInsnNode(ILOAD, 1));
					list.add(new VarInsnNode(ILOAD, 2));
					list.add(new VarInsnNode(ILOAD, 3));
					list.add(new VarInsnNode(ALOAD, 8));
					list.add(new VarInsnNode(ILOAD, 9));
					list.add(new VarInsnNode(ALOAD, 4));
					list.add(new VarInsnNode(ILOAD, 5));
					list.add(new MethodInsnNode(INVOKESTATIC, "nova/wrapper/mc1710/asm/StaticForwarder", "chunkSetBlockEvent", "(Lnet/minecraft/world/chunk/Chunk;IIILnet/minecraft/block/Block;Inet/minecraft/block/Block;I)V"));

					AbstractInsnNode lastInsn = method.instructions.getLast();
					while (lastInsn instanceof LabelNode || lastInsn instanceof LineNumberNode) {
						lastInsn = lastInsn.getPrevious();
					}

					if (ASMHelper.isReturn(lastInsn)) {
						method.instructions.insertBefore(lastInsn, list);
					} else {
						method.instructions.insert(list);
					}

					System.out.println("[NOVA] Injected instruction to method: " + m.s_name);
					break;
				}
			}

			return ASMHelper.createBytes(cnode, 0);
		}

		return bytes;
	}

}
