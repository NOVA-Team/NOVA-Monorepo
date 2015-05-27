package nova.wrapper.mc1710.asm.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.launchwrapper.IClassTransformer;
import nova.wrapper.mc1710.asm.lib.ASMHelper;

import org.objectweb.asm.tree.ClassNode;

public final class Transformers implements IClassTransformer {

	private static Map<String, List<Transformer>> transformers = new HashMap<>();

	public Transformers() {
		registerTransformer(new ChunkTransformer(), "net.minecraft.world.chunk.Chunk");
	}

	public static void registerTransformer(Transformer transformer, String... classes) {
		Objects.requireNonNull(classes);
		for (String clazz : classes) {
			List<Transformer> list = transformers.getOrDefault(clazz, new ArrayList<>());
			list.add(transformer);
			transformers.put(clazz, list);
		}
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformers.containsKey(transformedName)) {
			ClassNode cnode = ASMHelper.createClassNode(basicClass);
			transformers.get(transformedName).forEach(t -> t.transform(cnode));
			return ASMHelper.createBytes(cnode, 0);
		}
		return basicClass;
	}
}
