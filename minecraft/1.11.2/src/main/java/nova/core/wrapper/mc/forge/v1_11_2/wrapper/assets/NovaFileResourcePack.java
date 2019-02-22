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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.assets;

import com.google.common.base.Charsets;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.util.ResourceLocation;
import nova.core.language.LanguageManager;
import nova.core.wrapper.mc.forge.v1_11_2.NovaMinecraftPreloader;
import nova.internal.core.Game;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NovaFileResourcePack extends FileResourcePack implements NovaResourcePack<ZipEntry> {
	private final String modid;
	private final String[] domains;
	private ZipFile resourcePackZipFile;

	public NovaFileResourcePack(File file, String modid, String[] domains) {
		super(file);
		this.modid = modid;
		this.domains = domains;
	}

	private ZipFile getResourcePackZipFile() throws IOException {
		if (this.resourcePackZipFile == null) {
			this.resourcePackZipFile = new ZipFile(this.resourcePackFile);
		}

		return this.resourcePackZipFile;
	}

	@Override
	public Set<String> getResourceDomains() {
		HashSet<String> domains = new HashSet<>();
		domains.add(modid);
		domains.addAll(Arrays.asList(this.domains));
		return domains;
	}

	@Override
	protected InputStream getInputStreamByName(String path) throws IOException {
		path = transform(path);
		try {
			return getInputStreamCaseInsensitive(path);
		} catch (IOException e) {
			if (path.endsWith("sounds.json")) {
				return new ByteArrayInputStream(NovaMinecraftPreloader.generateSoundJSON(this).getBytes(Charsets.UTF_8));
			} else if ("pack.mcmeta".equalsIgnoreCase(path)) {
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
	public InputStream getInputStream(ResourceLocation rl) throws IOException {
		return getInputStreamByName(transform(rl));
	}

	@Override
	public boolean hasResourceName(String path) {
		path = transform(path);
		//Hack Sounds and McMeta
		if (path.endsWith("sounds.json") || path.endsWith("pack.mcmeta")) {
			return true;
		}

		return findFileCaseInsensitive(path).isPresent();
	}

	@Override
	public boolean resourceExists(ResourceLocation rl) {
		//Hack Sounds and McMeta
		if (rl.getResourcePath().endsWith("sounds.json") || rl.getResourcePath().endsWith("pack.mcmeta")) {
			return true;
		}

		return findFileCaseInsensitive(transform(rl)).isPresent();
	}

	@Override
	public Set<ResourceLocation> getLanguageFiles() {
		Pattern langPattern = Pattern.compile("^assets/([^/]+)/(lang/[a-zA-Z0-9-]+\\.lang)$", Pattern.CASE_INSENSITIVE);

		try {
			return getResourcePackZipFile().stream()
				.map(e -> {
					Matcher m = langPattern.matcher(e.getName());
					if (!m.matches())
						return null;
					return new ResourceLocation(m.group(1), m.group(2));
				})
				.filter(e -> e != null)
				.collect(Collectors.toSet());
		} catch (IOException ex) {
			return Collections.emptySet();
		}
	}

	@Override
	public String getID() {
		return modid;
	}

	@Override
	public InputStream getInputStreamCaseInsensitive(String path) throws IOException {
		Optional<ZipEntry> ze = findFileCaseInsensitive(transform(path));
		if (ze.isPresent())
			return getResourcePackZipFile().getInputStream(ze.get());
		return super.getInputStreamByName(path);
	}

	@Override
	public Optional<ZipEntry> findFileCaseInsensitive(String path) {
		String p = transform(path);
		try {
			return getResourcePackZipFile().stream().filter(e -> e.getName().equalsIgnoreCase(p)).findFirst().map(e -> (ZipEntry) e);
		} catch (IOException ex) {
			return Optional.empty();
		}
	}
}
