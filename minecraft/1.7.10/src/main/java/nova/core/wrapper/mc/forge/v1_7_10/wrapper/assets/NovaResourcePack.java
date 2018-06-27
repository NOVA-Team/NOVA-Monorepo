/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.assets;

import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import nova.core.util.Identifiable;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

/**
 * @author ExE Boss
 */
public interface NovaResourcePack<FileType> extends Identifiable, IResourcePack {

	default String transform(ResourceLocation rl) {
		return transform(String.format("assets/%s/%s", rl.getResourceDomain(), toAbsolutePath(rl.getResourcePath())));
	}

	default String transform(String path) {
		return toAbsolutePath(path.toLowerCase().replace('\\', '/').replaceFirst("^assets/minecraft", "assets/" + getID()));
	}

	@Override
	default String getPackName() {
		return getClass().getSimpleName() + ':' + getID();
	}

	Set<ResourceLocation> getLanguageFiles();

	InputStream getInputStreamCaseInsensitive(String path) throws IOException;

	Optional<FileType> findFileCaseInsensitive(String path);

	static String toAbsolutePath(String path) {
		LinkedList<String> stack = new LinkedList<>();
		int skipCount = 0;
		String[] split = path.split("[/\\\\]");

		for (int i = split.length - 1; i >= 0; i--) {
			String p = split[i];
			if (".".equals(p))
				continue;
			else if ("..".equals(p)) {
				skipCount++;
				continue;
			}

			if (skipCount > 0) {
				skipCount--;
				continue;
			}

			stack.addFirst(p);
		}

		if (skipCount > 0)
			throw new IllegalArgumentException('\'' + path + "' leads outside root");

		return String.join("/", stack);
	}
}
