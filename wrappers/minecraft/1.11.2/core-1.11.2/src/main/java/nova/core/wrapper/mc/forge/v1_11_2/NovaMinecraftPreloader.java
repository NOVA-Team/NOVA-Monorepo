/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License VERSION 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either VERSION 3 of the License, or
 * (at your option) any later VERSION.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.wrapper.mc.forge.v1_11_2;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import nova.core.loader.Mod;
import nova.core.util.ClassLoaderUtil;
import nova.core.wrapper.mc.forge.v1_11_2.launcher.NovaMinecraft;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.assets.NovaFileResourcePack;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.assets.NovaFolderResourcePack;
import nova.core.wrapper.mc.forge.v1_11_2.util.ReflectionUtil;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.assets.NovaResourcePack;
import nova.internal.core.Game;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

public class NovaMinecraftPreloader extends DummyModContainer {
	public static final String VERSION = "0.0.1";
	private static final ModMetadata md;
	public static Set<Class<?>> modClasses;
	public static Map<Class<?>, File> modClassToFile;
	public static List<NovaResourcePack<?>> novaResourcePacks = Collections.emptyList();

	static {
		md = new ModMetadata();
		md.modId = "novapreloader";
		md.name = "NOVA Preloader";
		md.version = VERSION;
	}

	public NovaMinecraftPreloader() {
		super(md);
	}

	/**
	 * Dynamically generates a sound JSON file based on a resource pack's file structure.
	 *
	 * If it's a folder, then load it as a sound collection.
	 * A sound collection falls under the same resource name. When called to play, it will pick a random sound from the collection to play it.
	 *
	 * If it's just a sound file, then load the sound file
	 *
	 * @param pack The resource pack to generate the sound JSON for.
	 * @return The generated sound JSON.
	 */
	public static String generateSoundJSON(AbstractResourcePack pack) {
		StringWriter sw = new StringWriter();
		try (JsonGenerator json = Json.createGenerator(sw);) {
			json.writeStartObject();
			if (pack instanceof FileResourcePack) {
				//For zip resource packs
				try {
					generateSoundJSON((FileResourcePack) pack, json);
				} catch (Exception e) {
					Error error = new ExceptionInInitializerError("Error generating fake sound JSON file.");
					error.addSuppressed(e);
					throw error;
				}
			} else if (pack instanceof FolderResourcePack) {
				//For folder resource packs
				generateSoundJSON((FolderResourcePack) pack, json);
			}
			json.writeEnd().flush();
			return sw.toString();
		}
	}

	private static JsonGenerator generateSoundJSON(FileResourcePack pack, JsonGenerator json) throws IOException {
		try (ZipFile zipFile = new ZipFile(pack.resourcePackFile)) {
			for (String domain : pack.getResourceDomains()) {
				//Load all sounds in the assets/domain/sounds/*
				if (getZipEntryForResourcePack(pack, "assets/" + domain + "/sounds/") != null) {
					String prefix = "assets/" + domain + "/sounds/";
					zipFile.stream()
						.filter(e -> e.getName().toLowerCase().startsWith(prefix.toLowerCase()) && !e.getName().equalsIgnoreCase(prefix))
						.forEach(e -> {
							String soundName = e.getName().replaceFirst(prefix, "").replaceFirst("\\.[^\\.]+$", "");
							if (soundName.contains("/"))
								return;

							json.writeStartObject(soundName);
							json.write("category", "ambient");
							json.writeStartArray("sounds");

							if (e.isDirectory()) {
								zipFile.stream()
									.filter(e2 -> e2.getName().startsWith(prefix + soundName + "/") && !e2.isDirectory())
									.map(ZipEntry::getName)
									.map(s -> s.replaceFirst(prefix + soundName + "/", "").replaceFirst("\\.[^\\.]+$", ""))
									.forEach(s -> json.write(soundName + "/" + s));
							} else {
								json.write(soundName);
							}
							json.writeEnd().writeEnd();
						});
				}
			}
		}

		return json;
	}

	private static JsonGenerator generateSoundJSON(FolderResourcePack pack, JsonGenerator json) {
		for (String domain : pack.getResourceDomains()) {
			//Load all sounds in the assets/domain/sounds/*
			File folder = getFileForResourcePack(pack, "assets/" + domain + "/sounds/");
			if (folder.exists()) {
				for (File listedFile : folder.listFiles()) {
					String soundName = listedFile.getName().replaceFirst("\\.[^\\.]+$", "");
					json.writeStartObject(soundName);
					json.write("category", "ambient");
					json.writeStartArray("sounds");
					if (listedFile.isFile()) {
						json.write(soundName);
					} else if (listedFile.isDirectory()) {
						for (File soundItemFile : listedFile.listFiles())
							json.write(soundName + "/" + soundItemFile.getName().replaceFirst("\\.[^\\.]+$", ""));
					}
					json.writeEnd().writeEnd();
				}
			}
		}

		return json;
	}

	public static String generatePackMcmeta() {
		StringWriter sw = new StringWriter();
		try (JsonGenerator json = Json.createGenerator(sw);) {
			json.writeStartObject()                                      //	{
			        .writeStartObject("pack")                            //		"pack": {
			            .write("description", "NOVA mod resource pack")  //			"description": "NOVA mod resource pack",
			            .write("pack_format", 3)                         //			"pack_format": 3 // Required by 1.11+
			        .writeEnd()                                          //		}
			    .writeEnd()                                              //	}
			.flush();

			return sw.toString();
		}
	}

	public static ZipEntry getZipEntryForResourcePack(FileResourcePack pack, String path) throws IOException {
		if (pack instanceof NovaFileResourcePack) {
			Optional<ZipEntry> entry = ((NovaFileResourcePack) pack).findFileCaseInsensitive(path);
			if (entry.isPresent())
				return entry.get();
		}

		try (ZipFile zf = new ZipFile(pack.resourcePackFile)) {
			return zf.getEntry(path);
		}
	}

	public static File getFileForResourcePack(FolderResourcePack pack, String path) {
		if (pack instanceof NovaFolderResourcePack) {
			Optional<File> file = ((NovaFolderResourcePack) pack).findFileCaseInsensitive(path);
			if (file.isPresent())
				return file.get();
		}

		return new File(pack.resourcePackFile, path);
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void load(FMLConstructionEvent event) {
		try {
			//TODO: Use AT
			//Apache Commons Hack
			Field launchHandlerField = FMLLaunchHandler.class.getDeclaredField("INSTANCE");
			launchHandlerField.setAccessible(true);
			FMLLaunchHandler launchHandler = (FMLLaunchHandler) launchHandlerField.get(null);
			Field clField = FMLLaunchHandler.class.getDeclaredField("classLoader");
			clField.setAccessible(true);
			LaunchClassLoader classLoader = (LaunchClassLoader) clField.get(launchHandler);
			//Obfuscation?
			Field setField = LaunchClassLoader.class.getDeclaredField("classLoaderExceptions");
			setField.setAccessible(true);
			@SuppressWarnings("unchecked")
			Set<String> classLoaderExceptions = (Set<String>) setField.get(classLoader);
			classLoaderExceptions.remove("org.apache.");
			Game.logger().info("Successfully hacked 'org.apache' out of launcher exclusion");
		} catch (Exception e) {
			throw new ClassLoaderUtil.ClassLoaderException(e);
		}

		// Scan mod classes
		ASMDataTable asmData = event.getASMHarvestedData();

		modClassToFile = new HashMap<>();
		modClasses = asmData
			.getAll(Mod.class.getName())
			.stream()
			.map(ASMDataTable.ASMData::getClassName)
			.map(c -> {
				try {
					return Class.forName(c);
				} catch (ClassNotFoundException e) {
					throw new ExceptionInInitializerError(e);
				}
			})
			.collect(Collectors.toSet());

		//Inject fake mod containers into FML
		List<ModContainer> fmlMods = ReflectionUtil.getPrivateObject(Loader.instance(), "mods");
		List<ModContainer> newMods = new ArrayList<>();
		newMods.addAll(fmlMods);
		newMods.stream().filter(mc -> NovaMinecraft.MOD_ID.equals(mc.getModId())).findFirst().ifPresent(nova -> md.parentMod = nova);
		modClasses.forEach(mod -> {
			ModMetadata fakeMeta = new ModMetadata();
			Mod annotation = mod.getAnnotation(Mod.class);
			fakeMeta.modId = annotation.id();
			fakeMeta.name = annotation.name();
			fakeMeta.version = annotation.version();
			fakeMeta.description = annotation.description();
			newMods.add(new NovaModContainer(fakeMeta, mod));
		});
		//TODO: Use AT
		ReflectionUtil.setPrivateObject(Loader.instance(), newMods, "mods");

		// Register resource packs
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			registerResourcePacks();
		}
	}

	public void registerResourcePacks() {
		Map<Mod, Class<?>> classesMap = modClasses
			.stream()
			.filter(clazz -> clazz.getAnnotation(Mod.class) != null)
			.collect(Collectors.toMap((clazz) -> clazz.getAnnotation(Mod.class), Function.identity()));

		try {
			// The same list exists in the Minecraft class, but that can be SRG or not.
			// Reflecting FML is just less work for us. (Minecraft.field_110449_ao)
			Field resourcePackField = FMLClientHandler.class.getDeclaredField("resourcePackList");
			resourcePackField.setAccessible(true);
			@SuppressWarnings("unchecked")
			List<IResourcePack> packs = (List<IResourcePack>) resourcePackField.get(FMLClientHandler.instance());

			Set<String> addedPacks = new HashSet<>();
			List<NovaResourcePack<?>> novaPacks = new LinkedList<>();

			classesMap.keySet().forEach(novaMod -> {
				Class<?> c = classesMap.get(novaMod);

				//Add jar resource pack
				String fn = c.getProtectionDomain().getCodeSource().getLocation().toExternalForm();

				if (fn.contains("!")) {
					fn = fn.substring(0, fn.indexOf('!')).replaceFirst("jar:", "");
					File file;
					try {
						file = new File(new URL(fn).toURI());
					} catch (MalformedURLException | URISyntaxException e) {
						file = new File(fn); //This will probably not work on Windows, but we can at least try
					}
					modClassToFile.put(c, file);

					if (!addedPacks.contains(fn)) {
						addedPacks.add(fn);
						NovaFileResourcePack pack = new NovaFileResourcePack(file, novaMod.id(), novaMod.domains());
						packs.add(pack);
						novaPacks.add(pack);
						Game.logger().info("Registered NOVA jar resource pack: {}", fn);
					}
				} else {
					//Add folder resource pack location. The folderLocation is the root of the project, including the packages of classes, and an assets folder inside.
					String folderLocation = c.getProtectionDomain().getCodeSource().getLocation().getPath();
					String classPath = c.getCanonicalName().replace('.', '/');
					folderLocation = folderLocation.replaceFirst("file:", "").replace(classPath, "").replace("/.class", "").replaceAll("%20", " ");
					File folderFile = new File(folderLocation);
					if (!new File(folderFile, "assets").isDirectory()) {
						//Try IDEA workaround.
						folderFile = new File(folderLocation.replaceAll("build[\\\\/]classes", "build/resources"));
						folderFile = new File(folderFile, "assets").isDirectory() ? folderFile : new File(folderLocation);
					}
					modClassToFile.put(c, folderFile);

					addedPacks.add(folderLocation);
					NovaFolderResourcePack pack = new NovaFolderResourcePack(folderFile, novaMod.id(), novaMod.domains());
					packs.add(pack);
					novaPacks.add(pack);
					Game.logger().info("Registered NOVA folder resource pack: {}", folderFile.getAbsolutePath());
				}
			});
			resourcePackField.set(FMLClientHandler.instance(), packs);
			novaResourcePacks = new ArrayList<>(novaPacks);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * A fake NovaMod to inject into FML.
	 */
	private static class NovaModContainer extends DummyModContainer {
		private final ModMetadata meta;
		private final Class<?> mod;
		private File source = null;

		public NovaModContainer(ModMetadata meta, Class<?> mod) {
			super(meta);
			this.meta = meta;
			this.mod = mod;
		}

		@Override
		public File getSource() {
			if (this.source == null) {
				this.source = NovaMinecraftPreloader.modClassToFile.get(mod);
			}
			return this.source;
		}
	}
}
