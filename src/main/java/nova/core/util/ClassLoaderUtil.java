package nova.core.util;

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
			throw new ClassLoaderException("Failed to add " + url + " to the classpath");
		}
	}

	public static void addFileToClassPath(File file) {
		try {
			addURLToClassPath(file.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new ClassLoaderException("Failed to convert " + file.getAbsolutePath() + " to URL");
		}
	}

	private static synchronized void setAddURL() {
		try {
			Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			m.setAccessible(true);
			addURL = m;
		} catch (NoSuchMethodException e) {
			throw new ClassLoaderException("URLClassLoader has no addURL method. All of my wat.", e);
		}
	}

	public static class ClassLoaderException extends NovaException {
		public ClassLoaderException() {
			super();
		}

		public ClassLoaderException(String message, Object... parameters) {
			super(message, parameters);
		}

		public ClassLoaderException(String message) {
			super(message);
		}

		public ClassLoaderException(String message, Throwable cause) {
			super(message, cause);
		}

		public ClassLoaderException(Throwable cause) {
			super(cause);
		}
	}
}
