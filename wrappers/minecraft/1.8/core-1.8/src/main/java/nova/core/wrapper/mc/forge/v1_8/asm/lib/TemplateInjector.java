/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.wrapper.mc.forge.v1_8.asm.lib;

import com.google.common.collect.HashBiMap;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The template injection manager is capable of injecting default interfaces into classes.
 * The default interface will have no overrides, thereby implementing the default methods.
 * @author Calclavia, ChickenBones
 */
public class TemplateInjector {

	public static final TemplateInjector instance = new TemplateInjector();
	private HashBiMap<String, InjectionTemplate> templates = HashBiMap.create();

	/**
	 * Registers a class to be injected by a default interface
	 * @param className - Class that will be injected
	 * @param template - Default interface used as a template to inject in the templateName
	 */
	public void registerTemplate(String className, Class<?> template) {
		templates.put(className, new InjectionTemplate(template.getName()));
	}

	private ClassNode getClassNode(String name) {
		try {
			return ASMHelper.createClassNode(ASMHelper.cl.getClassBytes(name.replace('/', '.')));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public class InjectionTemplate {
		/**
		 * The default interface class name that acts as a template.
		 */
		public final String templateName;

		/**
		 * The methods to be injected upon inject(ClassNode cnode);
		 */
		public ArrayList<MethodNode> methodImplementations = new ArrayList<>();

		public InjectionTemplate(String templateName) {
			this.templateName = templateName;

			/**
			 * Read the class node, scanning all the method implementations
			 */
			ClassNode cnode = getClassNode(templateName);

			for (MethodNode method : cnode.methods) {
				methodImplementations.add(method);
				method.desc = new ObfMapping(cnode.name, method.name, method.desc).toRuntime().s_desc;
			}
		}

		/**
		 * Patches the cnode withPriority the methods from this template.
		 * @param cnode The ClassNode instance
		 * @return If the class node was modified
		 */
		public boolean inject(ClassNode cnode) {

			/**
			 * Inject the interface
			 */
			String interfaceByteName = templateName.replace(".", "/");

			if (!cnode.interfaces.contains(interfaceByteName)) {
				cnode.interfaces.add(interfaceByteName);
			}

			boolean changed = false;

			List<String> names = new LinkedList<>();

			for (MethodNode method : cnode.methods) {
				ObfMapping m = new ObfMapping(cnode.name, method.name, method.desc).toRuntime();
				names.add(m.s_name + m.s_desc);
			}

			for (MethodNode impl : this.methodImplementations) {
				//Ignore the constructor
				if (!impl.name.equals("<init>")) {

					//If the method is ALREADY implemented, then skip it.
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