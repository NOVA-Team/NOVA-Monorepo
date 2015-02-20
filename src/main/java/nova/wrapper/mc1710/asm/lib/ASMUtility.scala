package nova.wrapper.mc1710.asm.lib

import org.objectweb.asm.tree.{ClassNode, InsnNode, MethodNode}
import org.objectweb.asm.{Opcodes, Type}

import scala.collection.JavaConversions._

/**
 * @since 21/04/14
 * @author tgame14
 */
object ASMUtility {
	def findOrCreateClinit(cnode: ClassNode): MethodNode = {
		for (mnode <- cnode.methods) {
			if (mnode.name.equals("<clinit>")) {
				return mnode
			}
		}
		val clinit: MethodNode = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "<clinit>", Type.getMethodDescriptor(Type.VOID_TYPE), null, null)
		clinit.instructions.add(new InsnNode(Opcodes.RETURN));
		cnode.methods.add(clinit);
		return clinit;
	}
}
