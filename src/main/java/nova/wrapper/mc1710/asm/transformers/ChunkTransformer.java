package nova.wrapper.mc1710.asm.transformers;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
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

public class ChunkTransformer implements Transformer {

	@Override
	public void transform(ClassNode cnode) {
		System.out.println("[NOVA] Transforming Chunk class for chunkModified event.");

		MethodNode method = ASMHelper.findMethod(new ObfMapping("net/minecraft/world/chunk/Chunk", "func_150807_a", "(IIILnet/minecraft/block/Block;I)Z"), cnode);

		System.out.println("[NOVA] Found method " + method.name);

		InsnList list = new InsnList();
		list.add(new VarInsnNode(ALOAD, 0));
		list.add(new VarInsnNode(ILOAD, 1));
		list.add(new VarInsnNode(ILOAD, 2));
		list.add(new VarInsnNode(ILOAD, 3));
		list.add(new VarInsnNode(ALOAD, 8));
		list.add(new VarInsnNode(ILOAD, 9));
		list.add(new VarInsnNode(ALOAD, 4));
		list.add(new VarInsnNode(ILOAD, 5));
		list.add(new MethodInsnNode(INVOKESTATIC, "nova/wrapper/mc1710/asm/StaticForwarder", "chunkSetBlockEvent", "(Lnet/minecraft/world/chunk/Chunk;IIILnet/minecraft/block/Block;ILnet/minecraft/block/Block;I)V", false));

		AbstractInsnNode lastInsn = method.instructions.getLast();
		while (lastInsn instanceof LabelNode || lastInsn instanceof LineNumberNode) {
			lastInsn = lastInsn.getPrevious();
		}

		if (ASMHelper.isReturn(lastInsn)) {
			method.instructions.insertBefore(lastInsn, list);
		} else {
			method.instructions.insert(list);
		}

		System.out.println("[NOVA] Injected instruction to method: " + method.name);
	}
}
