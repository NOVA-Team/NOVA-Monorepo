package nova.wrapper.mc1710.asm.lib;

import com.google.common.collect.HashBiMap;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Calclavia, ChickenBones
 */
public class TemplateInjectionManager {
	static HashBiMap<String, InjectionTemplate> tileTemplates = HashBiMap.create();
	static HashBiMap<String, InjectionTemplate> itemTemplates = HashBiMap.create();

	public static void registerTileTemplate(String name, Class templateClass, Class... templateInterfaces) {
		List<String> interfaces = new ArrayList<String>();

		for (Class templateInterface : templateInterfaces) {
			interfaces.add(templateInterface.getName());
		}

		tileTemplates.put(name, new InjectionTemplate(templateClass.getName(), interfaces));
	}

	public static void registerItemTemplate(String name, Class templateClass, Class... templateInterfaces) {
		List<String> interfaces = new ArrayList<String>();

		for (Class templateInterface : templateInterfaces) {
			interfaces.add(templateInterface.getName());
		}

		itemTemplates.put(name, new InjectionTemplate(templateClass.getName(), interfaces));
	}

	private static ClassNode getClassNode(String name) {
		try {
			return ASMHelper.createClassNode(TemplateTransformer.cl.getClassBytes(name.replace('/', '.')));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static class InjectionTemplate {
		/**
		 * The Java class name.
		 */
		public final String className;
		public final List<String> interfaces;

		/**
		 * The methods to be injected upon patch(ClassNode cnode);
		 */
		public ArrayList<MethodNode> methodImplementations = new ArrayList<MethodNode>();

		public InjectionTemplate(String className, List<String> interfaces) {
			this.className = className;
			this.interfaces = interfaces;

			ClassNode cnode = getClassNode(className);

			for (MethodNode method : cnode.methods) {
				this.methodImplementations.add(method);
				method.desc = new ObfMapping(cnode.name, method.name, method.desc).toRuntime().s_desc;
			}
		}

		/**
		 * Patches the cnode with the methods from this template.
		 *
		 * @param cnode
		 * @return
		 */
		public boolean patch(ClassNode cnode, boolean injectConstructor) {
			for (String interfaceName : this.interfaces) {
				String interfaceByteName = interfaceName.replace(".", "/");

				if (!cnode.interfaces.contains(interfaceByteName)) {
					cnode.interfaces.add(interfaceByteName);
				} else {
					return false;
				}
			}

			boolean changed = false;

			LinkedList<String> names = new LinkedList<String>();

			for (MethodNode method : cnode.methods) {
				ObfMapping m = new ObfMapping(cnode.name, method.name, method.desc).toRuntime();
				names.add(m.s_name + m.s_desc);
			}

			for (MethodNode impl : this.methodImplementations) {
				if (!impl.name.equals("<init>") || injectConstructor) {
					/**
					 * If the method is ALREADY implemented, then skip it.
					 */
					if (names.contains(impl.name + impl.desc)) {
						continue;
					}

					ObfMapping mapping = new ObfMapping(cnode.name, impl.name, impl.desc).toRuntime();
					MethodNode copy = new MethodNode(impl.access, mapping.s_name, mapping.s_desc, impl.signature,
						impl.exceptions == null ? null : impl.exceptions.toArray(new String[impl.exceptions.size()]));
					ASMHelper.copy(impl, copy);
					cnode.methods.add(impl);
					changed = true;
				}
			}

			return changed;
		}
	}

}