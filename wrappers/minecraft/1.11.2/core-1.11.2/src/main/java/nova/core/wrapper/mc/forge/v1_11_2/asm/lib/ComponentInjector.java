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

package nova.core.wrapper.mc.forge.v1_11_2.asm.lib;

import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.component.Passthrough;
import nova.core.network.NetworkTarget.Side;
import nova.core.util.ClassLoaderUtil;
import nova.internal.core.Game;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The ComponentInjector is capable of creating dynamic classes that implement a
 * specified super class and implement the interfaces specified by
 * {@link Component} and {@link Passthrough}.
 * @param <T> The constructed type
 * @author Vic Nightfall
 */
public class ComponentInjector<T> implements Opcodes {

	private Class<T> baseClazz;

	/**
	 * Cache, contains already created class objects for later use
	 */
	private Map<List<Class<? extends Component>>, Class<? extends T>> cache = new HashMap<>();

	public ComponentInjector(Class<T> baseClazz) {
		this.baseClazz = baseClazz;
	}

	public synchronized T inject(ComponentProvider<?> provider, Class<?>[] typeArgs, Object[] args) {
		try {
			List<Component> components = provider.components().stream()
				.filter(component -> component.getClass().getAnnotationsByType(Passthrough.class).length > 0)
				.collect(Collectors.toList());

			if (components.size() > 0) {
				List<Class<? extends Component>> componentClazzes = components.stream().map(c -> c.getClass()).collect(Collectors.toList());
				Game.logger().info("{} {}", Side.get(), componentClazzes);
				if (cache.containsKey(componentClazzes))
				// Cached class
				{
					return inject(cache.get(componentClazzes).getConstructor(typeArgs).newInstance(args), provider);
				} else {
					Class<? extends T> clazz = construct(componentClazzes);
					cache.put(componentClazzes, clazz);
					return inject(clazz.getConstructor(typeArgs).newInstance(args), provider);
				}

			} else {
				// No components withPriority passthrough interfaces, we can use the
				// base class.
				return baseClazz.getConstructor(typeArgs).newInstance(args);
			}
		} catch (Exception e) {
			throw new ClassLoaderUtil.ClassLoaderException("Failed to construct wrapper class for " + baseClazz, e);
		}
	}

	private T inject(T instance, ComponentProvider<?> provider) throws ReflectiveOperationException {
		Field f = instance.getClass().getDeclaredField("$$_provider");
		f.setAccessible(true);
		f.set(instance, provider);
		f.setAccessible(false);
		return instance;
	}

	private Class<? extends T> construct(List<Class<? extends Component>> components) {

		// Map components to specified wrapped interfaces
		Map<Class<?>, Class<? extends Component>> intfComponentMap = new HashMap<>();
		for (Class<? extends Component> component : components) {
			for (Passthrough pt : component.getAnnotationsByType(Passthrough.class)) {
				Class<?> intf;
				try {
					intf = Class.forName(pt.value());
				} catch (ClassNotFoundException exec) {
					throw new ClassLoaderUtil.ClassLoaderException("Invalid passthrough \"%s\" on component %s, the specified interface doesn't exist.", pt.value(), component);
				}
				if (!intf.isAssignableFrom(component)) {
					throw new ClassLoaderUtil.ClassLoaderException("Invalid passthrough \"%s\" on component %s, the specified interface isn't implemented.", pt.value(), component);
				}
				if (intfComponentMap.containsKey(intf)) {
					throw new ClassLoaderUtil.ClassLoaderException("Duplicate Passthrough interface found: %s (%s, %s)", pt.value(), component, intfComponentMap.get(intf));
				}
				intfComponentMap.put(intf, component);
			}
		}

		// Create new ClassNode from cached bytes
		ClassNode clazzNode = new ClassNode();
		String name = Type.getInternalName(baseClazz);
		String classname = name + "_$$_NOVA_" + cache.size();

		// Inject block field
		clazzNode.visit(V1_8, ACC_PUBLIC | ACC_SUPER, classname, null, name, intfComponentMap.keySet().stream().map(Type::getInternalName).toArray(s -> new String[s]));
		clazzNode.visitField(ACC_PRIVATE | ACC_FINAL, "$$_provider", Type.getDescriptor(ComponentProvider.class), null, null).visitEnd();

		// Add constructors
		for (Constructor<?> constructor : baseClazz.getConstructors()) {
			int mod = constructor.getModifiers();
			String descr = Type.getConstructorDescriptor(constructor);

			if (Modifier.isFinal(mod) || Modifier.isPrivate(mod)) {
				continue;
			}
			MethodVisitor mv = clazzNode.visitMethod(mod, "<init>", descr, null, ASMHelper.getExceptionTypes(constructor));

			// Call super constructor
			mv.visitCode();
			// load this
			mv.visitVarInsn(ALOAD, 0);
			Class<?>[] parameters = constructor.getParameterTypes();
			for (int i = 0; i < constructor.getParameterCount(); i++) {
				// variables
				mv.visitVarInsn(Type.getType(parameters[i]).getOpcode(ILOAD), i + 1);
			}
			mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(baseClazz), "<init>", descr, false);
			// return
			mv.visitInsn(RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		// Add methods
		for (Class<?> intf : intfComponentMap.keySet()) {
			// Create class constant
			Type clazzConst = Type.getType(intf.getClass());

			for (Method m : intf.getMethods()) {
				boolean isVoid = m.getReturnType() == null;
				String descr = Type.getMethodDescriptor(m);

				MethodVisitor mv = clazzNode.visitMethod(ACC_PUBLIC, m.getName(), descr, null, ASMHelper.getExceptionTypes(m));
				mv.visitCode();

				// load block instance
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, classname, "$$_provider", Type.getDescriptor(ComponentProvider.class));
				mv.visitLdcInsn(clazzConst);
				// load component instance
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(ComponentProvider.class), "get", Type.getMethodDescriptor(Type.getType(Component.class), Type.getType(Class.class)), false);

				// add parameters
				Class<?>[] parameters = m.getParameterTypes();
				for (int i = 0; i < m.getParameterCount(); i++) {
					mv.visitVarInsn(Type.getType(parameters[i]).getOpcode(ILOAD), i + 1);
				}

				// invoke
				mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(intf), m.getName(), descr, true);
				mv.visitInsn(isVoid ? RETURN : Type.getType(m.getReturnType()).getOpcode(IRETURN));
				mv.visitMaxs(0, 0);
				mv.visitEnd();
			}
		}

		clazzNode.visitEnd();

		return ASMHelper.defineClass(clazzNode, ClassWriter.COMPUTE_MAXS, baseClazz.getProtectionDomain());
	}
}
