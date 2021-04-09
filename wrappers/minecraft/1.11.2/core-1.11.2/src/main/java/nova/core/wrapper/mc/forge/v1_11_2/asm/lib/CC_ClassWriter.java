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

import org.objectweb.asm.ClassWriter;

/**
 * @author ChickenBones
 */
public class CC_ClassWriter extends ClassWriter {
	private final boolean runtime;

	public CC_ClassWriter(int flags) {
		this(flags, false);
	}

	public CC_ClassWriter(int flags, boolean runtime) {
		super(flags);
		this.runtime = runtime;
	}

	@Override
	protected String getCommonSuperClass(String type1, String type2) {
		String c = type1.replace('/', '.');
		String d = type2.replace('/', '.');
		if (ClassHeirachyManager.classExtends(d, c)) {
			return type1;
		}
		if (ClassHeirachyManager.classExtends(c, d)) {
			return type2;
		}
		do {
			c = ClassHeirachyManager.getSuperClass(c, runtime);
		}
		while (!ClassHeirachyManager.classExtends(d, c));
		return c.replace('.', '/');
	}
}
