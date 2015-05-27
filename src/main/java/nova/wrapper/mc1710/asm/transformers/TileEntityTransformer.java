package nova.wrapper.mc1710.asm.transformers;

import nova.wrapper.mc1710.asm.lib.ASMHelper;
import nova.wrapper.mc1710.asm.lib.InstructionComparator.InsnListSection;
import nova.wrapper.mc1710.asm.lib.ObfMapping;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TileEntityTransformer implements Transformer {

	@Override
	public void transform(ClassNode cnode) {

		System.out.println("[NOVA] Transforming TileEntity class for dynamic instance injection.");

		MethodNode method = ASMHelper.findMethod(new ObfMapping("net/minecraft/tileentity/TileEntity", "createAndLoadEntity", "(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/tileentity/TileEntity;"), cnode);

		ASMHelper.removeBlock(method.instructions, new InsnListSection(method.instructions, 23, 26));

		InsnList list = new InsnList();
		list.add(new VarInsnNode(ALOAD, 2));
		list.add(new MethodInsnNode(INVOKESTATIC, "nova/wrapper/mc1710/asm/StaticForwarder", "loadTileEntityHook", "(Ljava/lang/Class;)Lnet/minecraft/tileentity/TileEntity;", false));
		list.add(new VarInsnNode(ASTORE, 1));

		method.instructions.insert(method.instructions.get(22), list);
	}
}
