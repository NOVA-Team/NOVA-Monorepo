package nova.wrapper.mc18.asm.transformers;

import nova.wrapper.mc18.asm.lib.ASMHelper;
import nova.wrapper.mc18.asm.lib.InstructionComparator.InsnListSection;
import nova.wrapper.mc18.asm.lib.ObfMapping;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TileEntityTransformer implements Transformer {

	@Override
	public void transform(ClassNode cnode) {

		System.out.println("[NOVA] Transforming TileEntity class for dynamic instance injection.");

		ObfMapping obfmap = new ObfMapping("bcm", "c", "(Lfn;)Lbcm;");
		ObfMapping srgmap = new ObfMapping("net/minecraft/tileentity/TileEntity", "func_145827_c", "(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/tileentity/TileEntity;");
		ObfMapping decompMap = new ObfMapping("net/minecraft/tileentity/TileEntity", "createAndLoadEntity", "(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/tileentity/TileEntity;");

		MethodNode method = ASMHelper.findMethod(obfmap, cnode);

		if (method == null) {
			System.out.println("Lookup " + obfmap + " failed.");
			method = ASMHelper.findMethod(decompMap, cnode);

			if (method == null) {
				System.out.println("Lookup " + decompMap + " failed.");
				method = ASMHelper.findMethod(srgmap, cnode);
			}
		}

		System.out.println("Transforming method " + method.name);

		ASMHelper.removeBlock(method.instructions, new InsnListSection(method.instructions, 23, 26));

		InsnList list = new InsnList();
		list.add(new VarInsnNode(ALOAD, 0));
		list.add(new VarInsnNode(ALOAD, 2));
		list.add(new MethodInsnNode(INVOKESTATIC, "nova/wrapper/mc18/asm/StaticForwarder", "loadTileEntityHook", "(Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/Class;)Lnet/minecraft/tileentity/TileEntity;", false));
		list.add(new VarInsnNode(ASTORE, 1));

		method.instructions.insert(method.instructions.get(22), list);
	}
}
