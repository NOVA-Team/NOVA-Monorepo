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

package nova.core.wrapper.mc.forge.v17.render;

import com.google.common.base.Charsets;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.util.ResourceLocation;
import nova.core.wrapper.mc.forge.v17.NovaMinecraftPreloader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NovaResourcePack extends FileResourcePack {
	private final String modid;
	private final String[] domains;

	public NovaResourcePack(File p_i1290_1_, String modid, String[] domains) {
		super(p_i1290_1_);
		this.modid = modid;
		this.domains = domains;
	}

	@Override
	public Set getResourceDomains() {
		HashSet<String> domains = new HashSet<>();
		domains.add(modid);
		domains.addAll(Arrays.asList(this.domains));
		return domains;
	}

	private String transform(String path) {
		return path.replaceFirst("assets[/\\\\]minecraft", modid);
	}

	@Override
	protected InputStream getInputStreamByName(String path) throws IOException {
		try {
			return super.getInputStreamByName(transform(path));
		} catch (IOException e) {
			if (path.endsWith("sounds.json")) {
				return new ByteArrayInputStream(NovaMinecraftPreloader.generateSoundJSON(this).getBytes(Charsets.UTF_8));
			} else if (path.equals("pack.mcmeta")) {
				return new ByteArrayInputStream(NovaMinecraftPreloader.generatePackMcmeta().getBytes(Charsets.UTF_8));
			} else {
				if (path.endsWith(".mcmeta")) {
					return new ByteArrayInputStream("{}".getBytes());
				}
				throw e;
			}
		}
	}

	@Override
	public boolean hasResourceName(String path) {
		return super.hasResourceName(transform(path));
	}

	@Override
	public boolean resourceExists(ResourceLocation rl) {
		//Hack Sounds and McMeta
		if (rl.getResourcePath().endsWith("sounds.json") || rl.getResourcePath().endsWith("pack.mcmeta")) {
			return true;
		}

		return super.resourceExists(rl);
	}
}
