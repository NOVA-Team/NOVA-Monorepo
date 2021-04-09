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

package nova.core.config;

import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;
import nova.core.util.ReflectionUtil;
import nova.core.util.collection.Tuple2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Configuration {

	private Configuration() {}

	private static final ConfigRenderOptions renderOpts =
		ConfigRenderOptions.defaults()
			.setOriginComments(false) // If true, adds `# hardcoded value` everywhere
			.setJson(false); // If true, generates valid JSON

	/*
	 * Hard reflection to add comments for HOCON rendering
	 */

	private static Field  originField    = null;
	private static Method appendComments = null;
	private static Class<?> abstractConfigValueClass;

	static {
		try {
			//And classes are not public sadly. Package name `impl` says for itself
			abstractConfigValueClass = Class.forName("com.typesafe.config.impl.AbstractConfigValue");

			originField = abstractConfigValueClass.getDeclaredField("origin");
			originField.setAccessible(true);

			appendComments = Class.forName("com.typesafe.config.impl.SimpleConfigOrigin").getDeclaredMethod("appendComments", List.class);
			appendComments.setAccessible(true);

			//Origins field is final, but we could use superhacks to get around that
			Field modifiers = Field.class.getDeclaredField("modifiers");
			modifiers.setAccessible(true);
			modifiers.setInt(originField, originField.getModifiers() ^ Modifier.FINAL);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	private static void addComment(ConfigValue val, String comment) {
		if (abstractConfigValueClass.isInstance(val) && originField != null && appendComments != null) {
			try {
				Object newOrigin = appendComments.invoke(val.origin(), Collections.singletonList(comment));
				originField.set(val, newOrigin);
			} catch (InvocationTargetException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static com.typesafe.config.Config handle(com.typesafe.config.Config config, Object holder, String pathOffset) {
		Map<String, Tuple2<Object, String>> defaults = new HashMap<>();

		if (!holder.getClass().isAnnotationPresent(ConfigHolder.class)) {
			throw new nova.core.config.ConfigException("No @ConfigHolder annotation on your config class!");
		}

		boolean scanHolders = holder.getClass().getAnnotation(ConfigHolder.class).value();

		Map<Field, Config> fields;

		if (holder.getClass().getAnnotation(ConfigHolder.class).useAll()) {
			fields = Arrays.stream(holder.getClass().getDeclaredFields())
				.filter(f -> !f.isSynthetic())
				.map(f -> new Tuple2<>(f, f.isAnnotationPresent(Config.class) ? f.getAnnotation(Config.class) : Config.DEFAULT))
				.collect(Collectors.toMap(t -> t._1, t -> t._2));
		} else {
			fields = ReflectionUtil.getAnnotatedFields(Config.class, holder.getClass());
		}

		for (Map.Entry<Field, Config> entry : fields.entrySet()) {
			Field field = entry.getKey();
			Config ann = entry.getValue();
			String path = pathOffset + ("".equals(ann.value()) ? "" : ann.value() + ".") + field.getName();

			field.setAccessible(true);

			if (field.getType().isAnnotationPresent(ConfigHolder.class)) {
				if (scanHolders) {
					try {
						config = config.withFallback(handle(config, field.get(holder), path + "."));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				} else {
					throw new nova.core.config.ConfigException("Scanning inner-objects is disabled for `%s`", path);
				}
			} else {
				Object def = null;
				Object value = null;
				boolean failed = false;
				try {
					def = field.get(holder);
					value = config.getAnyRef(path);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ConfigException e) {
					failed = true;
				}
				if (failed) {
					if (def != null) {
						defaults.put(path, new Tuple2<>(def, ann.comment()));
					}
				} else {
					try {
						field.set(holder, value);
					} catch (IllegalArgumentException e) {
						throw new nova.core.config.ConfigException("Field `%s` is of the wrong type!", field.getName());
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (!defaults.isEmpty()) {
			com.typesafe.config.Config merged = config;
			for (Map.Entry<String, Tuple2<Object, String>> entry : defaults.entrySet()) {
				ConfigValue val = ConfigValueFactory.fromAnyRef(entry.getValue()._1);
				String comment = entry.getValue()._2;
				if (!"".equals(comment)) {
					//TODO: Maybe `comment = Game.language().translate(comment);` ?
					if (!val.origin().comments().contains(comment)) {
						addComment(val, comment);
					}
				}
				merged = merged.withValue(entry.getKey(), val);
			}
			config = merged;
		}
		if (holder instanceof ConfigHandler) {
			Optional<com.typesafe.config.Config> handled = ((ConfigHandler) holder).handle(config);
			if (handled.isPresent())
				config = handled.get();
		}
		return config;
	}

	/**
	 * Loads config data from HOCON string.
	 *
	 * @param configData Valid HOCON config.
	 * @param holder     Object with {@code @ConfigHolder} annotation and {@code @Config}'s in it.
	 * @return Full config with added default data, represented as string.
	 */
	public static String load(String configData, Object holder) {
		com.typesafe.config.Config parsed = ConfigFactory.parseString(configData);
		com.typesafe.config.Config config = handle(parsed, holder, "");

		if (!parsed.equals(config)) {
			return config.root().render(renderOpts);
		}

		return configData;
	}

	/**
	 * Loads config data from HOCON. If any values wasn't there,
	 * then writes defaults for them back to the file.
	 * If file do not exist - creates it and writes all defaults.
	 *
	 * @param configFile File to load config from.
	 * @param holder     Object with {@code @ConfigHolder} annotation and {@code @Config}'s in it.
	 */
	 // mkdirs and createNewFile
	public static void load(File configFile, Object holder) {
		if (!configFile.exists()) {
			try {
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		com.typesafe.config.Config parsed = ConfigFactory.parseFile(configFile);
		com.typesafe.config.Config config = handle(parsed, holder, "");

		if (!parsed.equals(config)) {
			String hocon = config.root().render(renderOpts);
			try (FileWriter writer = new FileWriter(configFile)) {
				writer.write(hocon);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loads config data from InputStream, any values missing are just ignored(so your defaults stay at place).
	 *
	 * @param stream Input stream to load data from.
	 * @param holder Object with {@code @ConfigHolder} annotation and {@code @Config}'s in it.
	 */
	public static void load(InputStream stream, Object holder) {
		handle(ConfigFactory.parseReader(new InputStreamReader(stream)), holder, "");
	}

	/**
	 * Shortcut to load configs from URL.
	 *
	 * @param url    Given URL.
	 * @param holder Object with {@code @ConfigHolder} annotation and {@code @Config}'s in it.
	 */
	public static void load(URL url, Object holder) {
		try {
			load(url.openStream(), holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shortcut to load configs from URI. Just uses URI#toURL, be warned.
	 *
	 * @param uri    Given URI.
	 * @param holder Object with {@code @ConfigHolder} annotation and {@code @Config}'s in it.
	 */
	public static void load(URI uri, Object holder) {
		try {
			load(uri.toURL(), holder);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
