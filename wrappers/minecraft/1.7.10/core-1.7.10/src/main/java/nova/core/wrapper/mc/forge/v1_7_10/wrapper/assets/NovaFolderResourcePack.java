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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.assets;

import com.google.common.base.Charsets;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.util.ResourceLocation;
import nova.core.wrapper.mc.forge.v1_7_10.NovaMinecraftPreloader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class NovaFolderResourcePack extends FolderResourcePack implements NovaResourcePack<File> {
	private static final Pattern LANG_PATTERN = Pattern.compile("^[a-zA-Z0-9-]+\\.lang$", Pattern.CASE_INSENSITIVE);
	private final String modid;
	private final String[] domains;

	public NovaFolderResourcePack(File file, String modid, String[] domains) {
		super(file);
		this.modid = modid;
		this.domains = domains;
	}

	@Override
	public Set<String> getResourceDomains() {
		Set<String> domains = new HashSet<>();
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
	public String getPackName() {
		return NovaResourcePack.super.getPackName();
	}

	@Override
	public String getID() {
		return modid;
	}

	@Override
	public Set<ResourceLocation> getLanguageFiles() {
		Set<ResourceLocation> langFiles = new HashSet<>();

		for (String domain : getResourceDomains()) {
			findFileCaseInsensitive("assets/" + domain + "/lang/").filter(File::isDirectory).ifPresent(file -> {
				Arrays.stream(file.listFiles((dir, name) -> LANG_PATTERN.asPredicate().test(name)))
					.map(File::getName)
					.forEach(name -> langFiles.add(new ResourceLocation(domain, "lang/" + name)));
			});
		}

		return langFiles;
	}

	@Override
	public InputStream getInputStreamCaseInsensitive(String path) throws IOException {
		Optional<File> file = findFileCaseInsensitive(transform(path));
		if (file.isPresent())
			return new FileInputStream(file.get());
		return super.getInputStreamByName(path);
	}

	@Override
	public Optional<File> findFileCaseInsensitive(String path) {
		final String transformedPath = transform(path);

		File[] files = resourcePackFile.listFiles((dir, name) -> transform(name).equalsIgnoreCase(transformedPath));

		switch (files.length) {
			case 0:
				return Optional.empty();
			case 1:
				return Optional.of(files[0]);
			default:
				// We are on a case sensitive file system
				return Optional.of(new File(resourcePackFile, path)).filter(File::exists);
		}
	}
}
