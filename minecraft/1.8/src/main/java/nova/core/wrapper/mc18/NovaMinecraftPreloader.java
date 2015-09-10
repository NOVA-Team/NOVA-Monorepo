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

package nova.core.wrapper.mc18;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FileResourcePack;
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
import nova.core.wrapper.mc18.render.NovaFolderResourcePack;
import nova.core.wrapper.mc18.render.NovaResourcePack;
import nova.core.wrapper.mc18.util.ReflectionUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NovaMinecraftPreloader extends DummyModContainer {
	public static final String version = "0.0.1";
	private static final ModMetadata md;
	public static Set<Class<?>> modClasses;

	static {
		md = new ModMetadata();
		md.modId = "novapreloader";
		md.name = "NOVA Preloader";
		md.version = version;
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
	 */
	public static String generateSoundJSON(AbstractResourcePack pack) {
		JsonObject fakeSoundJSON = new JsonObject();

		for (String domain : (Set<String>) pack.getResourceDomains()) {

			if (pack instanceof FileResourcePack) {
				//For zip resource packs
				try {
					ZipFile zipFile = new ZipFile(pack.resourcePackFile);

					if (zipFile.getEntry("assets/" + domain + "/sounds/") != null) {
						Enumeration zipEntries = zipFile.entries();

						while (zipEntries.hasMoreElements()) {
							String zipPath = ((ZipEntry) zipEntries.nextElement()).getName();

							String prefix = "assets/" + domain + "/sounds/";
							if (zipPath.startsWith(prefix) && !zipPath.equals(prefix)) {
								String soundName = zipPath.replaceFirst(prefix, "").replaceFirst("[.][^.]+$", "");
								ZipEntry entry = zipFile.getEntry(zipPath);

								if (!soundName.contains("/")) {
									JsonObject sound = new JsonObject();
									sound.addProperty("category", "ambient");
									JsonArray sounds = new JsonArray();

									if (entry.isDirectory()) {
										//Sound Collection
										Enumeration zipEntries2 = zipFile.entries();
										while (zipEntries2.hasMoreElements()) {
											String zipPath2 = ((ZipEntry) zipEntries2.nextElement()).getName();

											if (zipPath2.startsWith(prefix + soundName + "/") && !zipFile.getEntry(zipPath2).isDirectory()) {
												String randomSoundName = zipPath2.replaceFirst(prefix + soundName + "/", "");
												sounds.add(new JsonPrimitive(soundName + "/" + randomSoundName.replaceFirst("[.][^.]+$", "")));
											}
										}
									} else {
										sounds.add(new JsonPrimitive(soundName));
									}
									sound.add("sounds", sounds);
									fakeSoundJSON.add(soundName, sound);
								}
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					throw new ExceptionInInitializerError("Error generating fake sound JSON file.");
				}
			} else {
				//For folder resource packs
				//Load all sounds in the assets/domain/sounds/*
				File folder = new File(pack.resourcePackFile, "assets/" + domain + "/sounds/");

				if (folder.exists()) {
					File[] listOfFiles = folder.listFiles();

					for (int i = 0; i < listOfFiles.length; i++) {
						File listedFile = listOfFiles[i];

						JsonObject sound = new JsonObject();
						sound.addProperty("category", "ambient");
						JsonArray sounds = new JsonArray();

						String listedName = listedFile.getName().replaceFirst("[.][^.]+$", "");
						if (listedFile.isFile()) {
							sounds.add(new JsonPrimitive(listedName));
						} else if (listedFile.isDirectory()) {
							for (File soundItemFile : listedFile.listFiles())
								sounds.add(new JsonPrimitive(listedName + "/" + soundItemFile.getName().replaceFirst("[.][^.]+$", "")));
						}

						sound.add("sounds", sounds);
						fakeSoundJSON.add(listedName, sound);
					}
				}
			}
		}

		return fakeSoundJSON.toString();
	}

	public static String generatePackMcmeta() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("description", "NOVA mod resource pack");
		jsonObject.addProperty("pack_format", "1");
		JsonObject outerObj = new JsonObject();
		outerObj.add("pack", jsonObject);
		return outerObj.toString();
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
			Set<String> classLoaderExceptions = (Set) setField.get(classLoader);
			classLoaderExceptions.remove("org.apache.");
			System.out.println("Successfully hacked 'org.apache' out of launcher exclusion");
		} catch (Exception e) {
			throw new ClassLoaderUtil.ClassLoaderException(e);
		}

		// Scan mod classes
		ASMDataTable asmData = event.getASMHarvestedData();

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
		modClasses.forEach(mod -> {
			ModMetadata fakeMeta = new ModMetadata();
			Mod annotation = mod.getAnnotation(Mod.class);
			fakeMeta.modId = annotation.id();
			fakeMeta.name = annotation.name();
			fakeMeta.version = annotation.version();
			fakeMeta.description = annotation.description();
			newMods.add(new DummyNovaMod(fakeMeta));
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
			List<IResourcePack> packs = (List<IResourcePack>) resourcePackField.get(FMLClientHandler.instance());

			Set<String> addedPacks = new HashSet<>();

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

					if (!addedPacks.contains(fn)) {
						addedPacks.add(fn);
						packs.add(new NovaResourcePack(file, novaMod.id(), novaMod.domains()));
						System.out.println("Registered NOVA jar resource pack: " + fn);
					}
				} else {
					//Add folder resource pack location. The folderLocation is the root of the project, including the packages of classes, and an assets folder inside.
					String folderLocation = c.getProtectionDomain().getCodeSource().getLocation().getPath();
					String classPath = c.getCanonicalName().replaceAll("\\.", "/");
					folderLocation = folderLocation.replaceFirst("file:", "").replace(classPath, "").replace("/.class", "").replaceAll("%20", " ");
					File folderFile = new File(folderLocation);
					if (!new File(folderFile, "assets").isDirectory()) {
						//Try IDEA workaround.
						folderFile = new File(folderLocation.replaceAll("build[\\\\/]classes", "build/resources"));
						folderFile = new File(folderFile, "assets").isDirectory() ? folderFile : new File(folderLocation);
					}

					addedPacks.add(folderLocation);
					packs.add(new NovaFolderResourcePack(folderFile, novaMod.id(), novaMod.domains()));
					System.out.println("Registered NOVA folder resource pack: " + folderFile.getAbsolutePath());
				}
			});
			resourcePackField.set(FMLClientHandler.instance(), packs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * A fake NovaMod to inject into FML.
	 */
	private static class DummyNovaMod extends DummyModContainer {
		public DummyNovaMod(ModMetadata meta) {
			super(meta);
		}
	}
}
