package nova.wrapper.mc1710.manager;

import cpw.mods.fml.common.discovery.ASMDataTable;
import net.minecraftforge.common.config.Configuration;
import nova.core.util.Config;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 * How to use the config Handler:
 * </p><p>
 * First, mark your field with &#64;Config
 * </p>
 * <code>&#64;Config(key = "a key", category = "a category") public static double aValue = 3.765D;</code>
 * <p>
 * this is all you need to do for the field itself,
 * </p><p>
 * next: Just before you call queueSave for your Configuration file, call this method:
 * </p><p>
 * ConfigHandler.sync(Configuration configObject, String namespace);
 * </p><p>
 * now do remember, The namespace is your mods namespace. for the ICBM mod it would be "icbm", for
 * Resonant Engine, it would be "calclavia" yet if you want to split config files, you can do that by
 * separating namespaces: for example, ICBM Sentries separate Config file, and ICBM Explosives
 * separate config file
 * </p>
 * <pre>
 * ConfigHandler.sync(icbmSentryConfigObject, "icbm.sentry");
 * ConfigHandler.sync(icbmExplosivesConfigObject, "icbm.explosion");
 * </pre>
 * 
 * @author tgame14
 * @since 09/03/14
 */
public final class ConfigManager {

	public static final ConfigManager instance = new ConfigManager();
	protected Set<Class> classes = new LinkedHashSet<>();

	public void load(ASMDataTable table) {
		Set<ASMDataTable.ASMData> configs = table.getAll(Config.class.getName());

		for (ASMDataTable.ASMData data : configs) {
			try {
				classes.add(Class.forName(data.getClassName()));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Syncs the fields annotated with @Config with the data saved inside the config file.
	 *
	 * @param config - The configuration file
	 * @param namespace - The base package to conduct the search
	 */
	public void sync(Configuration config, String namespace) {
		config.load();

		for (Class clazz : classes) {
			if (clazz.getName().startsWith(namespace)) {
				for (Field field : clazz.getDeclaredFields()) {
					Config cfg = field.getAnnotation(Config.class);

					if (cfg != null) {
						handleField(field, cfg, config);
					}
				}
			}
		}

		if (config.hasChanged()) {
			config.save();
		}
	}

	/**
	 * Syncs a specific object reference instead of a static class.
	 *
	 * @param obj - The configuration object instance
	 * @param config - The configuration file
	 */
	public void sync(Object obj, Configuration config) {
		config.load();

		for (Field field : obj.getClass().getDeclaredFields()) {
			Config cfg = field.getAnnotation(Config.class);

			if (cfg != null) {
				handleField(obj, field, cfg, config);
			}
		}

		if (config.hasChanged()) {
			config.save();
		}
	}

	private void handleField(Field field, Config cfg, Configuration config) {
		handleField(null, field, cfg, config);
	}

	private void handleField(Object obj, Field field, Config cfg, Configuration config) {
		try {
			// Set field and annotation data Handled before handing the write of field to config
			field.setAccessible(true);
			String key;

			if (cfg.key().isEmpty()) {
				//TODO: Convert camel case
				key = field.getName();
			} else {
				key = cfg.key();
			}

			String comment = !cfg.comment().isEmpty() ? cfg.comment() : null;

			// if field is Array, use Config lists, otherwise use default config read and writes
			if (!field.getType().isArray()) {
				if (field.getType() == Integer.TYPE) {
					int value = config.get(cfg.category(), key, field.getInt(obj), comment).getInt(field.getInt(obj));
					field.setInt(obj, value);
				} else if (field.getType() == Double.TYPE) {
					double value = config.get(cfg.category(), key, field.getDouble(obj), comment).getDouble(field.getDouble(obj));
					field.setDouble(obj, value);
				} else if (field.getType() == Float.TYPE) {
					float value = (float) config.get(cfg.category(), key, field.getFloat(obj), comment).getDouble(field.getDouble(obj));
					field.setFloat(obj, value);
				} else if (field.getType() == String.class) {
					String value = config.get(cfg.category(), key, (String) field.get(obj), comment).getString();
					field.set(obj, value);
				} else if (field.getType() == Boolean.TYPE) {
					boolean value = config.get(cfg.category(), key, field.getBoolean(obj), comment).getBoolean(field.getBoolean(obj));
					field.setBoolean(obj, value);
				} else if (field.getType() == Long.TYPE) {
					//TODO: Use longs
					long value = config.get(cfg.category(), key, field.getLong(obj), comment).getInt();
					field.setLong(obj, value);
				}
			} else {
				if (field.getType().getComponentType() == String.class) {
					String[] values = config.get(cfg.category(), key, field.get(obj) != null ? (String[]) field.get(obj) : new String[0], comment).getStringList();
					field.set(obj, values);
				} else if (field.getType().getComponentType() == int.class) {
					int[] values = config.get(cfg.category(), key, field.get(obj) != null ? (int[]) field.get(obj) : new int[0], comment).getIntList();
					field.set(obj, values);
				} else if (field.getType().getComponentType() == boolean.class) {
					boolean[] values = config.get(cfg.category(), key, field.get(obj) != null ? (boolean[]) field.get(obj) : new boolean[0], comment).getBooleanList();
					field.set(obj, values);
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to sync: " + field.getName());
			e.printStackTrace();
		}
	}
}
