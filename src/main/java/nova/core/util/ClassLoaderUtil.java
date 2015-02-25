package nova.core.util;

import nova.core.util.exception.NovaException;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author rx14
 */
public class ClassLoaderUtil {
	private static Method addURL;

	public static void addURLToClassPath(URL url) {
		if (addURL != null) setAddURL();

		try {
			addURL.invoke(ClassLoader.getSystemClassLoader(), url);
		} catch (Throwable t) {
			throw new NovaException("Failed to add " + url + " to the classpath");
		}
	}

	public static void addFileToClassPath(File file) {
		try {
			addURLToClassPath(file.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new NovaException("Failed to convert " + file.getAbsolutePath() + " to URL");
		}
	}

	private static synchronized void setAddURL() {
		try {
			Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			m.setAccessible(true);
			addURL = m;
		} catch (NoSuchMethodException e) {
			throw new NovaException("URLClassLoader has no addURL method. All of my wat.", e);
		}
	}
}
