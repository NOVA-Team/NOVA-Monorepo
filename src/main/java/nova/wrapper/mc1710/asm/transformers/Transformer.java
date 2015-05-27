package nova.wrapper.mc1710.asm.transformers;

import org.objectweb.asm.tree.ClassNode;

public interface Transformer {

	public void transform(ClassNode cnode);
}
