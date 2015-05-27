package nova.wrapper.mc1710.asm.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public interface Transformer extends Opcodes {

	public void transform(ClassNode cnode);
}
