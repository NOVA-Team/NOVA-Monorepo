package nova.wrapper.mc1710.asm.transformers;

import nova.wrapper.mc1710.asm.lib.ASMHelper;
import nova.wrapper.mc1710.asm.lib.InstructionComparator.InsnListSection;
import nova.wrapper.mc1710.asm.lib.ObfMapping;
import org.objectweb.asm.tree.*;

public class TileEntityTransformer implements Transformer {

	@Override
	public void transform(ClassNode cnode) {

		System.out.println("[NOVA] Transforming TileEntity class for dynamic instance injection.");

		ObfMapping obfMap = new ObfMapping("aor", "c", "(Ldh;)Laor;");
		ObfMapping deobfMap = new ObfMapping("net/minecraft/tileentity/TileEntity", "createAndLoadEntity", "(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/tileentity/TileEntity;");

		MethodNode method = ASMHelper.findMethod(obfMap, cnode);

		if (method == null) {
			System.out.println("[NOVA] Lookup " + obfMap + " failed. You are probably in a deobf environment.");
			method = ASMHelper.findMethod(deobfMap, cnode);

			if (method == null) {
				System.out.println("[NOVA] Lookup " + deobfMap + " failed!");
			}
		}

		System.out.println("[NOVA] Transforming method " + method.name);

		ASMHelper.removeBlock(method.instructions, new InsnListSection(method.instructions, 23, 26));

		InsnList list = new InsnList();
		list.add(new VarInsnNode(ALOAD, 0));
		list.add(new VarInsnNode(ALOAD, 2));
		list.add(new MethodInsnNode(INVOKESTATIC, "nova/core/wrapper/mc17/asm/StaticForwarder", "loadTileEntityHook", "(Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/Class;)Lnet/minecraft/tileentity/TileEntity;", false));
		list.add(new VarInsnNode(ASTORE, 1));

		method.instructions.insert(method.instructions.get(22), list);
	}
}
